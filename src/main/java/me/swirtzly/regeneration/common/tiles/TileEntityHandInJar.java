package me.swirtzly.regeneration.common.tiles;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TileEntityHandInJar extends TileEntity implements ITickable, IInventory {

    public int lindosAmont = 0;
    private NonNullList<ItemStack> handInv = NonNullList.withSize(7, ItemStack.EMPTY);

    public int getLindosAmont() {
        return lindosAmont;
    }

    public void setLindosAmont(int lindosAmont) {
        this.lindosAmont = lindosAmont;
    }

    @Override
    public void update() {

        if (world.getWorldTime() % 35 == 0 && hasHand()) {
            world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), RegenObjects.Sounds.JAR_BUBBLES, SoundCategory.PLAYERS, 0.4F, 0.3F);
        }

        EntityPlayer player = world.getClosestPlayer(getPos().getX(), getPos().getY(), getPos().getZ(), 56, false);
        if (player != null) {
            IRegeneration data = CapabilityRegeneration.getForPlayer(player);
            if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                if (world.rand.nextInt(90) < 10) {
                    lindosAmont = lindosAmont + 1;
                }
            }
        }
    }

    public ItemStack getHand() {
        return handInv.get(3);
    }

    public boolean hasHand() {
        return handInv.get(3).getItem() == RegenObjects.Items.HAND;
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setFloat("lindos", lindosAmont);
        compound.setBoolean("hasHand", hasHand());
        ItemStackHelper.saveAllItems(compound, this.handInv);
        return super.writeToNBT(compound);
    }


    @Override
    public void readFromNBT(NBTTagCompound compound) {
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
        this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
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
        return getDisplayName().getUnformattedText();
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(RegenObjects.Blocks.HAND_JAR.getLocalizedName());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 3, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    public void sendUpdates() {
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
        markDirty();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }
}
