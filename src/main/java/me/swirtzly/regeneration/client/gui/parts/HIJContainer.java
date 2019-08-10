package me.swirtzly.regeneration.client.gui.parts;

import me.swirtzly.regeneration.handlers.RegenObjects;
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
        Slot s = new HandSlot(hijInvIn, 3, 44 + 2 * 18, 20) {
            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
            }
        };
        this.addSlotToContainer(s);

        for (int l = 0; l < 3; ++l) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 109));
        }
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

    public static class HandSlot extends Slot {

        public HandSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() == RegenObjects.Items.HAND;
        }
    }

}

