package me.fril.regeneration.common.items;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.PlayerUtil;
import me.fril.regeneration.util.RegenConfig;
import me.fril.regeneration.util.RegenObjects;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn) {
		
		IRegeneration capability = CapabilityRegeneration.getForPlayer(player);
		ItemStack stack = player.getHeldItem(handIn);
		
		if (capability.isCapable()) { //FIXME clean up this mess
			if (!player.isSneaking()) {
				int used = doUsageDamage(stack, capability);
				if (used == 0) {
					if (capability.getLivesLeft() == RegenConfig.regenCapacity) {
						PlayerUtil.sendMessage(player, "regeneration.messages.transfer.full_cycle", true);
					} else if (stack.getItemDamage() == RegenConfig.regenCapacity) {
						PlayerUtil.sendMessage(player, "regeneration.messages.transfer.empty_watch", true);
					}
					//NOTE there should probably be an else here that just errors stuff because that shouldn't be happening
					return new ActionResult<>(EnumActionResult.FAIL, stack);
				}
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.gained_regens", used), true); // too lazy to fix a single/plural issue here
			} else {
				if (stack.getItemDamage() == 0) {
					PlayerUtil.sendMessage(player, "regeneration.messages.transfer.full_watch", true);
					return new ActionResult<>(EnumActionResult.FAIL, stack);
				}
				
				stack.setItemDamage(stack.getItemDamage() - 1);
				capability.setLivesLeft(capability.getLivesLeft() - 1);
				PlayerUtil.sendMessage(player, "regeneration.messages.transfer.success", true);
				return new ActionResult<>(EnumActionResult.PASS, stack);
			}
		} else {
			if (!player.isSneaking()) {
				world.playSound(null, player.posX, player.posY, player.posZ, RegenObjects.Sounds.FOB_WATCH, SoundCategory.PLAYERS, 0.5F, 1.0F);
				doUsageDamage(stack, capability);
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.now_timelord"), true);
			} else {
				PlayerUtil.sendMessage(player, "regeneration.messages.transfer.empty_cycle", true);
				return new ActionResult<>(EnumActionResult.FAIL, stack);
			}
		}
		
		return super.onItemRightClick(world, player, handIn);
	}
	
	private int doUsageDamage(ItemStack stack, IRegeneration capability) {
		int supply = RegenConfig.regenCapacity - stack.getItemDamage(), needed = RegenConfig.regenCapacity - capability.getLivesLeft(), used = Math.min(supply, needed);
		if (used == 0)
			return 0;
		
		capability.setLivesLeft(capability.getLivesLeft() + used);
		capability.sync();
		
		if (!capability.getPlayer().isCreative())
			stack.setItemDamage(stack.getItemDamage() + used);
		return used;
	}
}