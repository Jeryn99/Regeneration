package me.suff.mc.regen.common.tiles;

import me.suff.mc.regen.handlers.RegenObjects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Created by Craig
 * on 22/04/2020 @ 17:43
 */
public class ArchTile extends TileEntity {
    public ArchTile() {
        super(RegenObjects.Tiles.ARCH.get());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(20);
    }
}
