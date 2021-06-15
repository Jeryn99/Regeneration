package me.suff.mc.regen.common.tiles;

import me.suff.mc.regen.common.block.JarBlock;
import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RParticles;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.objects.RTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Random;

public class JarTile extends TileEntity implements ITickableTileEntity {

    private boolean updateSkin = true;
    private ItemStackHandler itemHandler = createHandler();
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public JarTile() {
        super(RTiles.HAND_JAR.get());
    }

    private static void spawnParticles(World world, BlockPos worldIn) {
        Random random = world.random;

        for (Direction direction : Direction.values()) {
            BlockPos blockpos = worldIn.relative(direction);
            if (!world.getBlockState(blockpos).isSolidRender(world, blockpos)) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getStepX() : (double) random.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getStepY() : (double) random.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getStepZ() : (double) random.nextFloat();
                world.addParticle(RParticles.CONTAINER.get(), (double) worldIn.getX() + d1, (double) worldIn.getY() + d2, (double) worldIn.getZ() + d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public float getLindos() {
        return HandItem.getEnergy(getHand());
    }

    public void setLindos(float lindos) {
        lindos = MathHelper.clamp(lindos, 0, 100);
        HandItem.setEnergy(lindos, getHand());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(getBlockState(), pkt.getTag());
    }

    @Override
    public void tick() {
        if (isValid(Action.CREATE)) {
            spawnParticles(level, worldPosition);
        }
        if (level != null && level.isClientSide) return;

        if (isValid(Action.CREATE)) {
            if (level.getGameTime() % 77 == 0) {
                level.playSound(null, getBlockPos(), RSounds.JAR_BUBBLES.get(), SoundCategory.PLAYERS, 0.2F, 0.2F);
            }
        }

        if (level.getGameTime() % 100 == 0) {
            if (updateSkin) {
                setUpdateSkin(false);
            }
        }
    }

    public boolean isValid(Action action) {
        if (action == Action.ADD) {
            return getHand().getItem() == RItems.HAND.get() && getLindos() < 100;
        }
        if (action == Action.CREATE) {
            return getHand().getItem() == RItems.HAND.get() && getLindos() >= 100;
        }
        return false;
    }

    public void dropHandIfPresent(@Nullable PlayerEntity player) {
        if (!getHand().isEmpty()) {
            if (player != null) {
                if (!player.addItem(getHand())) {
                    InventoryHelper.dropItemStack(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), getHand());
                }
            } else {
                InventoryHelper.dropItemStack(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), getHand());
            }
            setHand(ItemStack.EMPTY);
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    public void sendUpdates() {
        if (level != null && level.getBlockState(worldPosition).getBlock() instanceof JarBlock) {
            level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(JarBlock.IS_OPEN, getHand().getItem() != RItems.HAND.get()));
        }
        level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
        setChanged();
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 3, getUpdateTag());
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        setLindos(nbt.getFloat("energy"));
        if (nbt.contains("inv")) {
            itemHandler.deserializeNBT(nbt.getCompound("inv"));
        }
        setUpdateSkin(nbt.getBoolean("update_skin"));
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putFloat("energy", getLindos());
        compound.put("inv", itemHandler.serializeNBT());
        compound.putBoolean("update_skin", updateSkin);
        return super.save(compound);
    }

    public boolean isUpdateSkin() {
        return updateSkin;
    }

    public void setUpdateSkin(boolean updateSkin) {
        this.updateSkin = updateSkin;
    }

    public ItemStack getHand() {
        return itemHandler.getStackInSlot(0);
    }

    // ==== Inventory ====

    public void setHand(ItemStack stack) {
        itemHandler.setStackInSlot(0, stack);
        setChanged();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
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
                setChanged();
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

    public enum Action {
        ADD, CREATE
    }
}
