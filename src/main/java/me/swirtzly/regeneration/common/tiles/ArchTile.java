package me.swirtzly.regeneration.common.tiles;

import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Created by Swirtzly
 * on 22/04/2020 @ 17:43
 */
public class ArchTile extends TileEntity {
    public ArchTile() {
        super(RegenObjects.Tiles.ARCH);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().grow(20);
    }
}
