package me.sub.common.items;

import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import me.sub.common.init.RObjects;
import me.sub.config.RegenConfig;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn) {

        IRegeneration capability = CapabilityRegeneration.get(player);
        ItemStack stack = player.getHeldItem(handIn);

        if (capability.isCapable()) {
            // TODO Store
        } else {
            world.playSound(null, player.posX, player.posY, player.posZ, RObjects.Sounds.FOB_WATCH, SoundCategory.PLAYERS, 0.5F, 1.0F);
            capability.setCapable(true);
            capability.setLivesLeft(12);
            setDamage(stack, stack.getItemDamage() + 1);
        }

        return super.onItemRightClick(world, player, handIn);
    }
}
