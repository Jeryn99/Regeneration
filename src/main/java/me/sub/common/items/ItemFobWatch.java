package me.sub.common.items;

import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class ItemFobWatch extends Item {

    public ItemFobWatch() {
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        IRegeneration capability = CapabilityRegeneration.get(playerIn);

        if (capability.isCapable()) {
            // TODO Store
        } else {
            //TODO play that cool ass sound
            capability.setCapable(true);
            capability.setLivesLeft(12);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
