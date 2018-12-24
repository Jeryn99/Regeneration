package me.fril.regeneration.common.items;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.util.ClientUtil;
import me.fril.regeneration.util.PlayerUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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
			if (stack.getItemDamage() == RegenConfig.regenCapacity)
				return msgUsageFailed(player, "regeneration.messages.transfer.empty_watch", stack);
			else if (cap.getRegenerationsLeft() == RegenConfig.regenCapacity)
				return msgUsageFailed(player, "regeneration.messages.transfer.max_regens", stack);
			
			int supply = RegenConfig.regenCapacity - stack.getItemDamage(),
				needed = RegenConfig.regenCapacity - cap.getRegenerationsLeft(),
				used = Math.min(supply, needed);
			
			if (cap.canRegenerate())
				PlayerUtil.sendHotbarMessage(player, new TextComponentTranslation("regeneration.messages.gained_regens", used), true);
			else if (world.isRemote) {
				RegenerationMod.DEBUGGER.getChannelFor(player).out(player.getName() + " is now a timelord");
				PlayerUtil.sendHotbarMessage(player, new TextComponentTranslation("regeneration.messages.now_timelord"), true);
			}
			
			if (used < 0)
				RegenerationMod.DEBUGGER.getChannelFor(player).warn("Fob watch used <0 regens (supply: "+supply+", needed:"+needed+", used:"+used+", capacity:"+RegenConfig.regenCapacity+", damage:"+stack.getItemDamage()+", regens:"+cap.getRegenerationsLeft());
			
			cap.receiveRegenerations(used);
			
			if (!cap.getPlayer().isCreative())
				stack.setItemDamage(stack.getItemDamage() + used);
			
			if (world.isRemote)
				ClientUtil.playPositionedSoundRecord(RegenObjects.Sounds.FOB_WATCH, 1.0F, 2.0F);
			
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		} else { //transferring player->watch
			if (!cap.canRegenerate())
				return msgUsageFailed(player, "regeneration.messages.transfer.no_regens", stack);
			
			if (stack.getItemDamage() == 0)
				return msgUsageFailed(player, "regeneration.messages.transfer.full_watch", stack);
			
			stack.setItemDamage(stack.getItemDamage() - 1);
			cap.extractRegeneration(1);
			PlayerUtil.sendHotbarMessage(player, "regeneration.messages.transfer.success", true);
			
			if (world.isRemote)
				ClientUtil.playPositionedSoundRecord(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 5.0F, 2.0F); //TODO change this sound
			
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
	}
	
	private ActionResult<ItemStack> msgUsageFailed(EntityPlayer player, String message, ItemStack stack) {
		PlayerUtil.sendHotbarMessage(player, message, true);
		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}
	
}
