package me.swirtzly.regeneration.util.common;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;

public class Worldutil {

    public static int getTopBlockForPos(World world, BlockPos spawn) {
        IChunk chunk = world.getChunk(spawn);
        return chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, spawn.getX(), spawn.getZ());
    }


}