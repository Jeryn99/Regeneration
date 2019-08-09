package me.swirtzly.regeneration.client.gui.parts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class HIJContainer extends Container {
    private final IInventory hijInv;

    public HIJContainer(IInventory playerInventory, IInventory hijInvIn, EntityPlayer player) {
        this.hijInv = hijInvIn;
        hijInvIn.openInventory(player);
        Slot s = new Slot(hijInvIn, 3, 44 + 2 * 18, 20) {
            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
            }
        };
        this.addSlotToContainer(s);
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return ItemStack.EMPTY;
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.hijInv.closeInventory(playerIn);
    }
}