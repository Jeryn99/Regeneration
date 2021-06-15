package me.suff.mc.regen.common.dimension.biomes;

import me.suff.mc.regen.common.dimension.features.BiomeHelper;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.awt.*;

/**
 * Created by Swirtzly
 * on 28/04/2020 @ 12:45
 */
public class GallifrayanWastelands extends Biome {

    public GallifrayanWastelands(Biome.Builder biomeBuilder) {
        super(biomeBuilder);
    }

    public GallifrayanWastelands() {
        super(new Biome.Builder().surfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState()))).precipitation(RainType.RAIN).biomeCategory(Category.PLAINS).downfall(0.3F).depth(0.6F).temperature(6F).waterColor(BiomeHelper.WASTELAND_WATER).waterFogColor(BiomeHelper.WASTELAND_WATER).scale(0.5F).parent(null));
        DefaultBiomeFeatures.addSwampClayDisk(this);
        DefaultBiomeFeatures.addSwampVegetation(this);
        DefaultBiomeFeatures.addDesertVegetation(this);
        DefaultBiomeFeatures.addSwampExtraDecoration(this);
        BiomeHelper.addBlackSpikes(this);
        BiomeHelper.addGallifreyOres(this);
        BiomeHelper.addSkulls(this);
        BiomeHelper.addHut(this);
    }

    @Override
    public int getGrassColor(BlockPos pos) {
        return new Color(156 / 255F, 108 / 255F, 61 / 255F).getRGB();
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        return BiomeHelper.GREY_LEAVES_COLOR;
    }


}
