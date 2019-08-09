package me.swirtzly.regeneration.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityHandInJar extends TileEntity implements ITickable, IInventory {

    public boolean isInUse = false;
    public int lindosAmont = 0;
    private AxisAlignedBB AABB = new AxisAlignedBB(0.2, 0, 0, 0.8, 2, 0.1);
    private NonNullList<ItemStack> handInv = NonNullList.withSize(7, ItemStack.EMPTY);


    public int getLindosAmont() {
        return lindosAmont;
    }

    public void setLindosAmont(int lindosAmont) {
        this.lindosAmont = lindosAmont;
    }

    public boolean isInUse() {
        return isInUse;
    }

    public void setInUse(boolean inUse) {
        isInUse = inUse;
    }

    @Override
    public void update() {

    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("inUse", isInUse);
        compound.setFloat("lindos", lindosAmont);
        ItemStackHelper.saveAllItems(compound, this.handInv);
        return super.writeToNBT(compound);
    }


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        isInUse = compound.getBoolean("inUse");
        lindosAmont = compound.getInteger("lindos");
        ItemStackHelper.loadAllItems(compound, this.handInv);
        super.readFromNBT(compound);
    }


    @Override
    public int getSizeInventory() {
        return handInv.size();
    }

    @Override
    public boolean isEmpty() {
        return handInv.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return handInv.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = this.handInv.get(index);

        if (index == 2 && !itemstack.isEmpty()) {
            return ItemStackHelper.getAndSplit(this.handInv, index, itemstack.getCount());
        } else {
            ItemStack itemstack1 = ItemStackHelper.getAndSplit(this.handInv, index, count);
            return itemstack1;
        }
    }


    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(handInv, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.handInv.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        handInv.clear();
    }

    @Override
    public String getName() {
        return "t";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
