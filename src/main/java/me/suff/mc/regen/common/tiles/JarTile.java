package me.suff.mc.regen.common.tiles;

import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.objects.RTiles;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class JarTile extends TileEntity implements ITickableTileEntity {

    private float lindos = 0F;
    private boolean updateSkin = true;
    private ItemStackHandler itemHandler = createHandler();
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public JarTile() {
        super(RTiles.HAND_JAR.get());
    }

    public void setLindos(float lindos) {
        lindos = MathHelper.clamp(lindos, 0, 100);
        HandItem.setEnergy(lindos, getHand());
    }

    public float getLindos() {
        return HandItem.getEnergy(getHand());
    }


    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(getBlockState(), pkt.getNbtCompound());
    }


    @Override
    public void tick() {

        if (world != null && world.isRemote) return;
        if (isValid(Action.CREATE) && world.getGameTime() % 77 == 0) {
            world.playSound(null, getPos(), RSounds.JAR_BUBBLES.get(), SoundCategory.PLAYERS, 0.2F, 0.2F);
        }

        if(world.getGameTime() % 100 == 0){
            if(updateSkin){
                setUpdateSkin(false);
            }
        }
    }

    public boolean isValid(Action action){
        if(action == Action.ADD) {
            return getHand().getItem() == RItems.HAND.get() && getLindos() < 100;
        }
        if(action == Action.CREATE){
            return getHand().getItem() == RItems.HAND.get() && getLindos() >= 100;
        }
        return false;
    }

    public void dropHandIfPresent() {
        if(!getHand().isEmpty()){
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), getHand());
            setHand(ItemStack.EMPTY);
        }
    }

    public enum Action {
        ADD, CREATE
    }


    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }


    public void sendUpdates() {
        world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        markDirty();
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 3, getUpdateTag());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        setLindos(nbt.getFloat("energy"));
        if(nbt.contains("inv")) {
            itemHandler.deserializeNBT(nbt.getCompound("inv"));
        }
        setUpdateSkin(nbt.getBoolean("update_skin"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putFloat("energy", getLindos());
        compound.put("inv", itemHandler.serializeNBT());
        compound.putBoolean("update_skin", updateSkin);
        return super.write(compound);
    }

    public void setUpdateSkin(boolean updateSkin) {
        this.updateSkin = updateSkin;
    }

    public boolean isUpdateSkin() {
        return updateSkin;
    }

    // ==== Inventory ====

    public ItemStack getHand() {
        return itemHandler.getStackInSlot(0);
    }

    public void setHand(ItemStack stack) {
        itemHandler.setStackInSlot(0, stack);
        markDirty();
    }

    @Override
    public void remove() {
        super.remove();
        handler.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == RItems.HAND.get();
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.getItem() != RItems.HAND.get()) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }
}
