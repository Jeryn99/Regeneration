package me.swirtzly.regeneration.common.dimension.biomes;

import me.swirtzly.regeneration.common.dimension.features.BiomeHelper;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.awt.*;

import static me.swirtzly.regeneration.common.dimension.features.BiomeHelper.WASTELAND_LEAVES;
import static me.swirtzly.regeneration.common.dimension.features.BiomeHelper.WASTELAND_WATER;

/**
 * Created by Swirtzly
 * on 28/04/2020 @ 12:45
 */
public class GallifrayanWastelands extends Biome {

    public GallifrayanWastelands(Biome.Builder biomeBuilder){
        super(biomeBuilder);
    }

    public GallifrayanWastelands() {
        super(new Biome.Builder().surfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.SANDSTONE.getDefaultState()))).precipitation(RainType.RAIN).category(Category.PLAINS).downfall(0.3F).depth(0.6F).temperature(6F).waterColor(WASTELAND_WATER).waterFogColor(WASTELAND_WATER).scale(0.5F).parent(null));
        DefaultBiomeFeatures.addSwampClayDisks(this);
        DefaultBiomeFeatures.addSwampVegetation(this);
        DefaultBiomeFeatures.addDeadBushes(this);
        DefaultBiomeFeatures.addFossils(this);
        BiomeHelper.addBlackSpikes(this);
        BiomeHelper.addGallifreyOres(this);
    }

    @Override
    public int getGrassColor(BlockPos pos) {
        return new Color(156 / 255F, 108 / 255F, 61 / 255F).getRGB();
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        return WASTELAND_LEAVES;
    }


}
