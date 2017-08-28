package com.afg.regeneration.items;

import com.afg.regeneration.Regeneration;
import com.afg.regeneration.superpower.TimelordHandler;
import lucraft.mods.lucraftcore.superpower.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpower.SuperpowerPlayerHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * Created by AFlyingGrayson on 8/28/17
 */
public class ChameleonArch extends Item
{
	public ChameleonArch() {
		this.setUnlocalizedName("chameleonArch");
		this.setRegistryName("chameleonarch");
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		SuperpowerPlayerHandler handler = SuperpowerHandler.getSuperpowerPlayerHandler(playerIn);
		if (handler == null){
			SuperpowerHandler.setSuperpower(playerIn, Regeneration.timelord);
			playerIn.sendStatusMessage(new TextComponentString("You've become a timelord! (animation coming soon)"), true);
		} else if (handler instanceof TimelordHandler){
			playerIn.sendStatusMessage(new TextComponentString("You've reset your regeneration cycles!"), true);
			((TimelordHandler) handler).regenCount = 0;
		}
		else return new ActionResult<>(EnumActionResult.FAIL, itemstack);

		return new ActionResult<>(EnumActionResult.PASS, ItemStack.EMPTY);
	}
}
