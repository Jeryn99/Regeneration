package com.afg.regeneration.items;

import com.afg.regeneration.capability.ITimelordCapability;
import com.afg.regeneration.capability.TimelordCapability;
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
public class ChameleonArch extends Item {
    public ChameleonArch() {
        this.setUnlocalizedName("chameleonArch");
        this.setRegistryName("chameleonarch");
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (playerIn.hasCapability(TimelordCapability.TIMELORD_CAP, null)) {
            ITimelordCapability capability = playerIn.getCapability(TimelordCapability.TIMELORD_CAP, null);
            if (capability.isTimelord()) {
                playerIn.sendStatusMessage(new TextComponentString("You've reset your regeneration cycles!"), true);
                capability.setRegenCount(0);
                capability.syncToPlayer();
            } else {
                playerIn.sendStatusMessage(new TextComponentString("You've become a timelord! (animation coming soon)"), true);
                capability.setTimelord(true);
                capability.syncToPlayer();
            }
        } else return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        return new ActionResult<>(EnumActionResult.PASS, ItemStack.EMPTY);
    }
}
