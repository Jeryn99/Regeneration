package me.swirtzly.regen.common.tiles;

import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.objects.RTiles;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;

public class JarTile extends TileEntity implements ITickableTileEntity {

    public JarTile() {
        super(RTiles.HAND_JAR.get());
    }

    @Override
    public void tick() {
        if (world.getGameTime() % 77 == 0) {
            world.playSound(null, getPos(), RSounds.JAR_BUBBLES.get(), SoundCategory.PLAYERS, 0.2F, 0.2F);
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(getBlockState(), pkt.getNbtCompound());
    }

    public void sendUpdates() {
        world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        markDirty();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }
}
