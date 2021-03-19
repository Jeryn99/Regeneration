package me.suff.mc.regen.common.world.biome.surface;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;

/* Created by Craig on 19/03/2021 */
public class DeathZoneSurfaceBuilder extends SurfaceBuilder< SurfaceBuilderConfig > {
    public DeathZoneSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    @Override
    public void apply(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
        SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, config);
    }


}
