package me.swirtzly.regeneration.client.gui.parts;

import me.swirtzly.regeneration.common.item.ItemArchInterface;
import me.swirtzly.regeneration.common.item.arch.capability.ArchInventory;
import me.swirtzly.regeneration.common.item.arch.capability.CapabilityArch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerArch extends Container {

    public static final int ID = 99;
    private final ArchInventory archInvIn;

    public ContainerArch(EntityPlayer player, ArchInventory archInv) {
        IInventory playerInv = player.inventory;
        archInvIn = archInv;

        addSlotToContainer(new SlotItemHandler(archInvIn, 0, 44 + 2 * 18, 20));

        for (int l = 0; l < 3; ++l) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(playerInv, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInv, i1, 8 + i1 * 18, 109));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = inventorySlots.get(index);
        if ((slot != null) && slot.getHasStack()) {
            final ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemArchInterface;
    }


}