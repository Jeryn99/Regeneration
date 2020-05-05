package me.swirtzly.regeneration.common.dimension.biomes;

import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import static me.swirtzly.regeneration.common.dimension.features.BiomeHelper.WASTELAND_WATER;

/**
 * Created by Swirtzly
 * on 05/05/2020 @ 14:56
 */
public class GallifreyanWastelandsMountains extends GallifrayanWastelands {

    public GallifreyanWastelandsMountains(){
        super(new Biome.Builder().surfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(Blocks.SAND.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.STONE.getDefaultState()))).precipitation(RainType.RAIN).category(Category.PLAINS).downfall(0.3F).depth(3.6F).temperature(6F).waterColor(WASTELAND_WATER).waterFogColor(WASTELAND_WATER).scale(3.6F).parent(null));
    }

}
