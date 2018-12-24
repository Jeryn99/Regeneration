package me.fril.regeneration.common.items;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.PlayerUtil;
import me.fril.regeneration.util.RegenConfig;
import me.fril.regeneration.util.RegenObjects;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
public class ItemFobWatch extends Item {
	
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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		ItemStack stack = player.getHeldItem(hand);
		
		if (!player.isSneaking()) { //transferring watch->player
			if (stack.getItemDamage() == RegenConfig.regenCapacity) {
				return msgUsageFailed(player, "regeneration.messages.transfer.empty_watch", stack);
			} else if (cap.getLivesLeft() == RegenConfig.regenCapacity) {
				return msgUsageFailed(player, "regeneration.messages.transfer.max_regens", stack);
			}
			
			int supply = RegenConfig.regenCapacity - stack.getItemDamage(),
					needed = RegenConfig.regenCapacity - cap.getLivesLeft(),
					used = Math.min(supply, needed);
			
			if (cap.isCapable())
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.gained_regens", used), true);
			else if (player.world.isRemote) {
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.now_timelord"), true);
			}
			
			if (used < 0) {
				cap.setLivesLeft(used);
			}
			
			if (!cap.getPlayer().isCreative())
				stack.setItemDamage(stack.getItemDamage() + used);
			
			if (world.isRemote)
				PlayerUtil.playMovingSound(cap.getPlayer(), RegenObjects.Sounds.FOB_WATCH, SoundCategory.PLAYERS, true);
			
		} else { //transferring player->watch
			if (!cap.isCapable())
				return msgUsageFailed(player, "regeneration.messages.transfer.no_regens", stack);
			
			if (stack.getItemDamage() == 0)
				return msgUsageFailed(player, "regeneration.messages.transfer.full_watch", stack);
			
			stack.setItemDamage(stack.getItemDamage() - 1);
			cap.setLivesLeft(cap.getLivesLeft() - 1);
			PlayerUtil.sendMessage(player, "regeneration.messages.transfer.success", true);
			
			if (world.isRemote)
				PlayerUtil.playMovingSound(cap.getPlayer(), SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.PLAYERS, true); //TODO change this sound
			
			return new ActionResult<>(EnumActionResult.PASS, stack);
		}
		
		return super.onItemRightClick(world, player, hand);
	}
	
	private ActionResult<ItemStack> msgUsageFailed(EntityPlayer player, String message, ItemStack stack) {
		PlayerUtil.sendMessage(player, message, true);
		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}
	
}