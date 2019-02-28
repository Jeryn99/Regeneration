package me.suff.regeneration.common.item;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.entity.EntityItemOverride;
import me.suff.regeneration.handlers.RegenObjects;
import me.suff.regeneration.util.ClientUtil;
import me.suff.regeneration.util.DebuggerUtil;
import me.suff.regeneration.util.PlayerUtil;
import me.suff.regeneration.RegenConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class ItemFobWatch extends ItemOverrideBase {
	
	public ItemFobWatch() {
		super(new Item.Properties().defaultMaxDamage(RegenConfig.CONFIG.regenCapacity.get()).group(ItemGroup.MISC));
		addPropertyOverride(new ResourceLocation("open"), (stack, worldIn, entityIn) -> {
			if (getStackTag(stack) == null || !getStackTag(stack).hasKey("open")) {
				return 0F; //Closed
			}
			return getOpen(stack);
		});
	}
	
	public static NBTTagCompound getStackTag(ItemStack stack) {
		if (stack.getTag() == null) {
			stack.setTag(new NBTTagCompound());
			stack.getTag().setInt("open", 0);
		}
		return stack.getTag();
	}
	
	public static int getOpen(ItemStack stack) {
		return getStackTag(stack).getInt("open");
	}
	
	public static void setOpen(ItemStack stack, int amount) {
		getStackTag(stack).setInt("open", amount);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		stack.setDamage(0);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getTag() == null) {
			stack.setTag(new NBTTagCompound());
			stack.getTag().setBoolean("live", false);
		} else {
			stack.getTag().setBoolean("live", false);
		}
		
		if (getOpen(stack) == 1) {
			if (entityIn.ticksExisted % 600 == 0) {
				setOpen(stack, 0);
			}
		}
		
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		ItemStack stack = player.getHeldItem(hand);
		
		if (!player.isSneaking()) { // transferring watch->player
			if (stack.getDamage() == RegenConfig.CONFIG.regenCapacity.get())
				return msgUsageFailed(player, "regeneration.messages.transfer.empty_watch", stack);
			else if (cap.getRegenerationsLeft() == RegenConfig.CONFIG.regenCapacity.get())
				return msgUsageFailed(player, "regeneration.messages.transfer.max_regens", stack);
			
			int supply = RegenConfig.CONFIG.regenCapacity.get() - stack.getDamage(), needed = RegenConfig.CONFIG.regenCapacity.get() - cap.getRegenerationsLeft(), used = Math.min(supply, needed);
			
			if (cap.canRegenerate()) {
				setOpen(stack, 1);
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.gained_regens", used), true);
			} else if (!world.isRemote) {
				DebuggerUtil.out(player, player.getName() + " is now a timelord");
				setOpen(stack, 1);
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.now_timelord"), true);
			}
			
			if (used < 0)
				DebuggerUtil.warn(player, "Fob watch used <0 regens (supply: " + supply + ", needed:" + needed + ", used:" + used + ", capacity:" + RegenConfig.CONFIG.regenCapacity.get() + ", damage:" + stack.getDamage() + ", regens:" + cap.getRegenerationsLeft());
			
			
			if (!cap.getPlayer().isCreative()) {
				stack.setDamage(stack.getDamage() + used);
			}
			
			if (world.isRemote) {
				setOpen(stack, 1);
				ClientUtil.playPositionedSoundRecord(RegenObjects.Sounds.FOB_WATCH, 1.0F, 2.0F);
			} else {
				cap.receiveRegenerations(used);
			}
			
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		} else { // transferring player->watch
			if (!cap.canRegenerate())
				return msgUsageFailed(player, "regeneration.messages.transfer.no_regens", stack);
			
			if (stack.getDamage() == 0)
				return msgUsageFailed(player, "regeneration.messages.transfer.full_watch", stack);
			
			stack.setDamage(stack.getDamage() - 1);
			PlayerUtil.sendMessage(player, "regeneration.messages.transfer.success", true);
			
			if (world.isRemote) {
				ClientUtil.playPositionedSoundRecord(SoundEvents.BLOCK_FIRE_EXTINGUISH, 5.0F, 2.0F);
			} else {
				setOpen(stack, 1);
				cap.extractRegeneration(1);
			}
			
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
		if (itemStack.getItem() == this && itemStack.getDamage() != RegenConfig.CONFIG.regenCapacity.get()) {
			if (itemOverride.ticksExisted % 5000 == 0 || itemOverride.ticksExisted == 2) {
				ClientUtil.playSound(itemOverride, RegenObjects.Sounds.FOB_WATCH_DIALOGUE.getRegistryName(), SoundCategory.AMBIENT, false, () -> !itemOverride.isAlive(), 1.5F);
			}
		}
	}
}
