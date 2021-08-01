package me.suff.mc.regen.common.tiles;

import me.suff.mc.regen.common.block.JarBlock;
import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RParticles;
import me.suff.mc.regen.common.objects.RSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BioContainerBlockEntity extends BlockEntity implements BlockEntityTicker<BioContainerBlockEntity> {

    private boolean updateSkin = true;
    private ItemStackHandler itemHandler = createHandler();
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public BioContainerBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    private static void spawnParticles(Level world, BlockPos worldIn) {
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
        lindos = Mth.clamp(lindos, 0, 100);
        HandItem.setEnergy(lindos, getHand());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, BioContainerBlockEntity bioContainerBlockEntity) {
        if (isValid(Action.CREATE)) {
            spawnParticles(level, worldPosition);
        }
        if (level != null && level.isClientSide) return;

        if (isValid(Action.CREATE)) {
            if (level.getGameTime() % 77 == 0) {
                level.playSound(null, getBlockPos(), RSounds.JAR_BUBBLES.get(), SoundSource.PLAYERS, 0.2F, 0.2F);
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

    public void dropHandIfPresent(@Nullable Player player) {
        if (!getHand().isEmpty()) {
            if (player != null) {
                if (!player.addItem(getHand())) {
                    Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), getHand());
                }
            } else {
                Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), getHand());
            }
            setHand(ItemStack.EMPTY);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
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
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(worldPosition, 3, getUpdateTag());
    }

    @Override
    public void load(CompoundTag nbt) {
        setLindos(nbt.getFloat("energy"));
        if (nbt.contains("inv")) {
            itemHandler.deserializeNBT(nbt.getCompound("inv"));
        }
        setUpdateSkin(nbt.getBoolean("update_skin"));
        super.load(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
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
