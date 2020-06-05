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

    private static final int SEARCHRADIUS = 16;
    public int lindosAmont = 0;
    private int othersAround = 0;
    private NonNullList<ItemStack> handInv = NonNullList.withSize(7, ItemStack.EMPTY);

    public int getLindosAmont() {
        return lindosAmont;
    }

    public void setLindosAmont(int lindosAmont) {
        this.lindosAmont = lindosAmont;
    }

    @Override
    public void update() {

        if (world.getWorldTime() % 77 == 0 && hasHand()) {
            world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), RegenObjects.Sounds.JAR_BUBBLES, SoundCategory.PLAYERS, 0.2F, 0.2F);
        }
        if (world.getWorldTime() % 200 == 0) {//update nearby containers
            othersAround = 0;
            for (int x = -SEARCHRADIUS; x <= SEARCHRADIUS; x++) {
                for (int y = -SEARCHRADIUS; y <= SEARCHRADIUS; y++) {
                    for (int z = -SEARCHRADIUS; z <= SEARCHRADIUS; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;//ignore itself
                        BlockPos bp = this.pos.add(x, y, z);
                        if (world.isBlockLoaded(bp) && world.getTileEntity(bp) instanceof TileEntityHandInJar) {
                            othersAround++;
                        }
                    }
                }
            }
        }

        EntityPlayer player = world.getClosestPlayer(getPos().getX(), getPos().getY(), getPos().getZ(), 8, false);
        if (player != null) {
            IRegeneration data = CapabilityRegeneration.getForPlayer(player);
            if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                if (world.rand.nextInt(90 + othersAround * 30) < 10) {
                    lindosAmont = lindosAmont + 1;
                }
            }
        }
    }

    public boolean hasHand() {
        return getHand().getItem() == RegenObjects.Items.HAND;
    }

    public ItemStack getHand() {
        return handInv.get(3);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        lindosAmont = compound.getInteger("lindos");
        ItemStackHelper.loadAllItems(compound, this.handInv);

        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setFloat("lindos", lindosAmont);
        compound.setBoolean("hasHand", hasHand());
        ItemStackHelper.saveAllItems(compound, this.handInv);
        return super.writeToNBT(compound);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 3, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(RegenObjects.Blocks.HAND_JAR.getLocalizedName());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
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
            return ItemStackHelper.getAndSplit(this.handInv, index, count);
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

    public void sendUpdates() {
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 7);
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
        markDirty();
    }
}
