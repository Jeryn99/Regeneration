package com.lcm.regeneration.items;

import com.lcm.regeneration.RegenerationSounds;
import com.lcm.regeneration.superpower.TimelordSuperpower;
import com.lcm.regeneration.superpower.TimelordSuperpowerHandler;

import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/** Created by AFlyingGrayson on 8/28/17 */
public class ItemChameleonArch extends Item {
	public ItemChameleonArch() {
		setUnlocalizedName("chameleonArch");
		setRegistryName("chameleonarch");
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(1);
		setMaxDamage(12);
	}
	
	@Override //TODO where did the "new life" message go?
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack arch = playerIn.getHeldItem(handIn);
		SuperpowerPlayerHandler handler = SuperpowerHandler.getSuperpowerPlayerHandler(playerIn);
		
		if (handler == null) {
			SuperpowerHandler.setSuperpower(playerIn, TimelordSuperpower.INSTANCE);
			
			int used = doUsageDamage(arch, SuperpowerHandler.getSpecificSuperpowerPlayerHandler(playerIn, TimelordSuperpowerHandler.class));
			if (arch.getCount() > 0) throw new RuntimeException("Did not fully use arch when receiving superpower ("+used+","+arch.getCount()+")");
			
			playerIn.world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, RegenerationSounds.GET, SoundCategory.PLAYERS, 1.0F, 1.0F);
			playerIn.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-regen.messages.becomeTimelord")), true);
		} else if (handler instanceof TimelordSuperpowerHandler) {
			TimelordSuperpowerHandler tmh = ((TimelordSuperpowerHandler) handler);

			int used = doUsageDamage(arch, tmh);
			if (used == 0) return new ActionResult<>(EnumActionResult.FAIL, arch);
			
			playerIn.world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, RegenerationSounds.GET, SoundCategory.PLAYERS, 1.0F, 1.0F);
			playerIn.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-regen.messages.gainedRegenerations", used)), true); //too lazy to fix a single/plural issue here
		} else return new ActionResult<>(EnumActionResult.FAIL, arch);
		
		return new ActionResult<>(EnumActionResult.PASS, arch);
	}
	
	private int doUsageDamage(ItemStack stack, TimelordSuperpowerHandler handler) {
		int supply = 12 - stack.getItemDamage(), needed = 12 - handler.regenerationsLeft, used = Math.min(supply, needed);
		
		if (used == 0) return 0;
		
		handler.regenerationsLeft += used;
		stack.setItemDamage(stack.getItemDamage() + used);
		if (stack.getItemDamage() == 12) stack.shrink(1);
		return used;
	}
}
