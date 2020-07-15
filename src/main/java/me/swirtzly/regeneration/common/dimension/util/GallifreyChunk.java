package me.swirtzly.regeneration.common.dimension.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.OverworldChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.server.ServerWorld;

public class GallifreyChunk extends OverworldChunkGenerator {
    private static final int SEALEVEL = 63;

    public GallifreyChunk(IWorld world, BiomeProvider provider) {
        super(world, provider, new OverworldGenSettings() {
            public BlockState getDefaultBlock() {
                return Blocks.DIRT.getDefaultState();
            }

            public BlockState getDefaultFluid() {
                return Blocks.WATER.getDefaultState();
            }
        });
        this.randomSeed.skip(5349);
    }

    @Override
    public int getSeaLevel() {
        return SEALEVEL;
    }

    @Override
    public void spawnMobs(ServerWorld worldIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs) {
    }
}

