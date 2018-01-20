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
			arch.shrink(1);
			playerIn.world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, RegenerationSounds.GET, SoundCategory.PLAYERS, 1.0F, 1.0F);
			playerIn.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-regen.messages.becomeTimelord")), true);
		} else if (handler instanceof TimelordSuperpowerHandler) {
			TimelordSuperpowerHandler tmh = ((TimelordSuperpowerHandler) handler);

			int supply = 12 - arch.getItemDamage(), needed = 12 - tmh.regenerationsLeft, used = Math.min(supply, needed);
			if (used == 0) return new ActionResult<>(EnumActionResult.FAIL, arch);
			
			tmh.regenerationsLeft += used;
			arch.setItemDamage(arch.getItemDamage() + used);
			if (arch.getItemDamage() == 12) arch.shrink(1);
			
			playerIn.world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, RegenerationSounds.GET, SoundCategory.PLAYERS, 1.0F, 1.0F);
			playerIn.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-regen.messages.gainedRegenerations", used)), true); //too lazy to fix a single/plural issue here
		} else return new ActionResult<>(EnumActionResult.FAIL, arch);
		
		return new ActionResult<>(EnumActionResult.PASS, arch);
	}
}
