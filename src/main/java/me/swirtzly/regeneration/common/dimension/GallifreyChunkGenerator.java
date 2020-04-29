package me.swirtzly.regeneration.common.dimension;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.OverworldChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;

public class GallifreyChunkGenerator extends OverworldChunkGenerator {

    public GallifreyChunkGenerator(IWorld worldIn, BiomeProvider provider, OverworldGenSettings settingsIn) {
        super(worldIn, provider, settingsIn);
    }
}