package me.fril.common.items;

import me.fril.common.capability.CapabilityRegeneration;
import me.fril.common.capability.IRegeneration;
import me.fril.common.init.RObjects;
import me.fril.config.RegenConfig;
import me.fril.util.PlayerUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class ItemFobWatch extends Item {

    public ItemFobWatch() {
        setMaxDamage(RegenConfig.Regen.regenCapacity);
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        stack.setItemDamage(RegenConfig.Regen.regenCapacity);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn) {

        IRegeneration capability = CapabilityRegeneration.get(player);
        ItemStack stack = player.getHeldItem(handIn);
        if (capability == null) return new ActionResult<>(EnumActionResult.PASS, stack);

        /*if (stack.getItemDamage() == RegenConfig.Regen.regenCapacity) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }*/

        if (capability.isCapable()) {
            if (!player.isSneaking()) {
                int used = doUsageDamage(stack, capability);
                if (used == 0) {
                    if (capability.getLivesLeft() == RegenConfig.Regen.regenCapacity) {
                        player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("regeneration.messages.transfer.fullCycle")), true);
                    } else if (stack.getItemDamage() == RegenConfig.Regen.regenCapacity)
                        player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("regeneration.messages.transfer.emptyWatch")), true);
                    //XXX there should probably be an else here that just errors stuff because that shouldn't be happening
                    return new ActionResult<>(EnumActionResult.FAIL, stack);
                }
                player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("regeneration.messages.gainedRegens", used)), true); // too lazy to fix a single/plural issue here
            } else {
                if (stack.getItemDamage() == 0) {
                    player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("regeneration.messages.transfer.fullWatch")), true);
                    return new ActionResult<>(EnumActionResult.FAIL, stack);
                } else if (capability.getLivesLeft() < 1) {
                    player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("regeneration.messages.transfer.emptyCycle")), true);
                    return new ActionResult<>(EnumActionResult.FAIL, stack);
                }
                
                stack.setItemDamage(stack.getItemDamage() - 1);
                capability.setLivesLeft(capability.getLivesLeft() - 1);
                player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("regeneration.messages.transfer.success")), true);
                return new ActionResult<>(EnumActionResult.PASS, stack);
            }
        } else {
            world.playSound(null, player.posX, player.posY, player.posZ, RObjects.Sounds.FOB_WATCH, SoundCategory.PLAYERS, 0.5F, 1.0F);
            capability.setCapable(true);
            doUsageDamage(stack, capability);
            PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.nowTimelord"), true);
        }

        return super.onItemRightClick(world, player, handIn);
    }

    private int doUsageDamage(ItemStack stack, IRegeneration capability) {
        int supply = RegenConfig.Regen.regenCapacity - stack.getItemDamage(), needed = RegenConfig.Regen.regenCapacity - capability.getLivesLeft(), used = Math.min(supply, needed);
        if (used == 0)
            return 0;

        capability.setLivesLeft(capability.getLivesLeft() + used);
        capability.sync();

        if (!capability.getPlayer().isCreative())
            stack.setItemDamage(stack.getItemDamage() + used);
        return used;
    }
}
