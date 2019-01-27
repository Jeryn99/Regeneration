package me.fril.regeneration.common.item;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.entity.EntityItemOverride;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.util.ClientUtil;
import me.fril.regeneration.util.DebuggerUtil;
import me.fril.regeneration.util.PlayerUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class ItemFobWatch extends ItemOverrideBase {
	
	public ItemFobWatch() {
		setMaxDamage(RegenConfig.regenCapacity);
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(1);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		stack.setItemDamage(RegenConfig.regenCapacity);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setBoolean("live", false);
		} else {
			stack.getTagCompound().setBoolean("live", false);
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		ItemStack stack = player.getHeldItem(hand);
		
		if (!player.isSneaking()) { // transferring watch->player
			if (stack.getItemDamage() == RegenConfig.regenCapacity)
				return msgUsageFailed(player, "regeneration.messages.transfer.empty_watch", stack);
			else if (cap.getRegenerationsLeft() == RegenConfig.regenCapacity)
				return msgUsageFailed(player, "regeneration.messages.transfer.max_regens", stack);
			
			int supply = RegenConfig.regenCapacity - stack.getItemDamage(),
					needed = RegenConfig.regenCapacity - cap.getRegenerationsLeft(),
					used = Math.min(supply, needed);
			
			if (cap.canRegenerate())
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.gained_regens", used), true);
			else if (!world.isRemote) {
				DebuggerUtil.out(player, player.getName() + " is now a timelord");
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.now_timelord"), true);
			}
			
			if (used < 0)
				DebuggerUtil.warn(player, "Fob watch used <0 regens (supply: " + supply + ", needed:" + needed + ", used:" + used + ", capacity:" + RegenConfig.regenCapacity + ", damage:" + stack.getItemDamage() + ", regens:" + cap.getRegenerationsLeft());
			
			
			if (!cap.getPlayer().isCreative())
				stack.setItemDamage(stack.getItemDamage() + used);
			
			if (world.isRemote)
				ClientUtil.playPositionedSoundRecord(RegenObjects.Sounds.FOB_WATCH, 1.0F, 2.0F);
			else
				cap.receiveRegenerations(used);
			
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		} else { // transferring player->watch
			if (!cap.canRegenerate())
				return msgUsageFailed(player, "regeneration.messages.transfer.no_regens", stack);
			
			if (stack.getItemDamage() == 0)
				return msgUsageFailed(player, "regeneration.messages.transfer.full_watch", stack);
			
			stack.setItemDamage(stack.getItemDamage() - 1);
			PlayerUtil.sendMessage(player, "regeneration.messages.transfer.success", true);
			
			if (world.isRemote)
				ClientUtil.playPositionedSoundRecord(SoundEvents.BLOCK_FIRE_EXTINGUISH, 5.0F, 2.0F);
			else
				cap.extractRegeneration(1);
			
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
	}
	
	private ActionResult<ItemStack> msgUsageFailed(EntityPlayer player, String message, ItemStack stack) {
		PlayerUtil.sendMessage(player, message, true);
		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}
	
	@Override
	public void update(EntityItemOverride itemOverride) {
		if (!itemOverride.world.isRemote) return;
		ItemStack itemStack = itemOverride.getItem();
		if (itemStack.getItem() == this && itemStack.getItemDamage() != RegenConfig.regenCapacity) {
			if (itemOverride.ticksExisted % 5000 == 0 || itemOverride.ticksExisted == 2) {
				ClientUtil.playSound(itemOverride, RegenObjects.Sounds.FOB_WATCH_DIALOGUE.getRegistryName(), SoundCategory.AMBIENT, false, () -> itemOverride.isDead, 1.5F);
			}
		}
	}
}