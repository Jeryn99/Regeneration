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
    private LazyOptional< IItemHandler > handler = LazyOptional.of(() -> itemHandler);

    public JarTile() {
        super(RTiles.HAND_JAR.get());
    }

    private static void spawnParticles(World world, BlockPos worldIn) {
        Random random = world.rand;

        for (Direction direction : Direction.values()) {
            BlockPos blockpos = worldIn.offset(direction);
            if (!world.getBlockState(blockpos).isOpaqueCube(world, blockpos)) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getXOffset() : (double) random.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getYOffset() : (double) random.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getZOffset() : (double) random.nextFloat();
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
        handleUpdateTag(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public void tick() {
        if (isValid(Action.CREATE)) {
            spawnParticles(world, pos);
        }
        if (world != null && world.isRemote) return;

        if (isValid(Action.CREATE)) {
            if (world.getGameTime() % 77 == 0) {
                world.playSound(null, getPos(), RSounds.JAR_BUBBLES.get(), SoundCategory.PLAYERS, 0.2F, 0.2F);
            }
        }

        if (world.getGameTime() % 100 == 0) {
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
                if (!player.addItemStackToInventory(getHand())) {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), getHand());
                }
            } else {
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), getHand());
            }
            setHand(ItemStack.EMPTY);
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    public void sendUpdates() {
        if (world != null && world.getBlockState(pos).getBlock() instanceof JarBlock) {
            world.setBlockState(pos, world.getBlockState(pos).with(JarBlock.IS_OPEN, getHand().getItem() != RItems.HAND.get()));
        }
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
        if (nbt.contains("inv")) {
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
        markDirty();
    }

    @Override
    public void remove() {
        super.remove();
        handler.invalidate();
    }

    @Nonnull
    @Override
    public < T > LazyOptional< T > getCapability(@Nonnull Capability< T > cap, @Nullable Direction side) {
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

    public enum Action {
        ADD, CREATE
    }
}
