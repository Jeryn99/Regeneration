package me.swirtzly.regeneration.common.item.arch.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class ArchInventory implements IItemHandlerModifiable {

    public final ItemStack arch;
    private final IItemHandlerModifiable archInv;

    public ArchInventory(ItemStack arch) {
        this.arch = arch;
        archInv = (IItemHandlerModifiable) arch.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        archInv.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return archInv.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return archInv.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return archInv.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return archInv.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return archInv.getSlotLimit(slot);
    }

}