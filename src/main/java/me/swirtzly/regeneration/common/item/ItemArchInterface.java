package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.RegenerationMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Swirtzly on 29/01/2020 @ 20:41
 */
public class ItemArchInterface extends Item {

    public IInventory inventory;

    public ItemArchInterface() {
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
        this.inventory = new InventoryBasic("Arch", true, 8);
    }

    /**
     * Called when the equipped item is right clicked.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(entityequipmentslot);

        playerIn.openGui(RegenerationMod.INSTANCE, 99, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);


        if (itemstack1.isEmpty()) {
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            //ArchHelper.onArchUse(playerIn, playerIn.getHeldItemOffhand());itemstack.setCount(0);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ICapabilityProvider() {

            @Override
            public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !stack.isEmpty();
            }


            @Nullable
            @Override
            public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                if (hasCapability(capability, facing)) {
                    return (T) new InvWrapper(inventory);
                }
                return null;
            }
        };
    }

    @Nullable
    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EntityEquipmentSlot.HEAD;
    }
}
