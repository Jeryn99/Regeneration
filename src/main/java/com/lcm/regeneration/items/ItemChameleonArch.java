package com.lcm.regeneration.items;

import com.lcm.regeneration.Regeneration;
import com.lcm.regeneration.common.capabilities.timelord.capability.CapabilityRegeneration;
import com.lcm.regeneration.common.capabilities.timelord.capability.IRegenerationCapability;
import com.lcm.regeneration.events.RObjects;
import com.lcm.regeneration.utils.RegenConfig;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemChameleonArch extends Item {

    public ItemChameleonArch() { // CHECK how should combining/repairing work out?
        setUnlocalizedName("chameleonArch");
        setRegistryName(Regeneration.MODID, "chameleonarch");
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
        setMaxDamage(RegenConfig.regenCapacity);
    }

    @Override public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack arch = player.getHeldItem(hand);
        IRegenerationCapability handler = player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
        System.out.println(handler);
        if(handler == null) return new ActionResult<>(EnumActionResult.PASS, arch);
        player.world.playSound(null, player.posX, player.posY, player.posZ, RObjects.SoundEvents.timeyWimey, SoundCategory.PLAYERS, 1.0F, 1.0F);

        if (arch.getItemDamage() == RegenConfig.regenCapacity) {
            player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("lcm-regen.messages.transfer.emptyArch")), true);
            return new ActionResult<>(EnumActionResult.FAIL, arch);
        }

        if (!handler.isTimelord()) {
            handler.setTimelord(true);
            doUsageDamage(arch, handler);
            player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("lcm-regen.messages.becomeTimelord")), true);
        } else {

            if (!player.isSneaking()) {
                int used = doUsageDamage(arch, handler);
                if (used == 0) {
                    if (handler.getRegensLeft() == RegenConfig.regenCapacity) {
                        player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("lcm-regen.messages.transfer.fullCycle", used)), true);
                    } else if (arch.getItemDamage() == RegenConfig.regenCapacity)
                        player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("lcm-regen.messages.transfer.emptyArch", used)), true);
                    return new ActionResult<>(EnumActionResult.FAIL, arch);
                }
                player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("lcm-regen.messages.gainedRegenerations", used)), true); // too lazy to fix a single/plural issue here
            } else {
                if (arch.getItemDamage() == 0 && !player.isCreative()) {
                    player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("lcm-regen.messages.transfer.fullArch")), true);
                    return new ActionResult<>(EnumActionResult.FAIL, arch);
                } else if (handler.getRegensLeft() < 1) {
                    player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("lcm-regen.messages.transfer.emptyCycle")), true);
                    return new ActionResult<>(EnumActionResult.FAIL, arch);
                }
                arch.setItemDamage(arch.getItemDamage() - 1);
                handler.setRegensLeft(handler.getRegensLeft() - 1);
                player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("lcm-regen.messages.transfer")), true);
                return new ActionResult<>(EnumActionResult.PASS, arch);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, arch);
    }

    private int doUsageDamage(ItemStack stack, IRegenerationCapability handler) {
        int supply = RegenConfig.regenCapacity - stack.getItemDamage(), needed = RegenConfig.regenCapacity - handler.getRegensLeft(), used = Math.min(supply, needed);
        if (used == 0)
            return 0;

        handler.setRegensLeft(handler.getRegensLeft() + used);
        handler.syncToAll();

        if (!handler.getPlayer().isCreative())
            stack.setItemDamage(stack.getItemDamage() + used);
        return used;
    }


    
}
