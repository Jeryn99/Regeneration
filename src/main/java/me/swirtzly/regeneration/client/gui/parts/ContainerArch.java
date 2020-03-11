package me.swirtzly.regeneration.client.gui.parts;

import me.swirtzly.regeneration.common.item.arch.capability.CapabilityArch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerArch extends Container {
    public static final int ID = 99;
    private final IInventory archInv;

    public ContainerArch(IInventory playerInventory, IInventory archInv, EntityPlayer player) {
        this.archInv = archInv;
        archInv.openInventory(player);
        Slot s = new ArchSlot(this.archInv, 3, 44 + 2 * 18, 20) {
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

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.archInv.closeInventory(playerIn);
    }

    public static class ArchSlot extends Slot {

        public ArchSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.hasCapability(CapabilityArch.CAPABILITY, null);
        }
    }
}
