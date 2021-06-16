package me.suff.mc.regen.common.dimension.biomes;

import me.suff.mc.regen.common.dimension.features.BiomeHelper;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

/**
 * Created by Craig
 * on 28/04/2020 @ 12:42
 */
public class GallifreyanRedLands extends Biome {

    public GallifreyanRedLands() {
        super(new Biome.Builder().surfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.STONE.defaultBlockState()))).precipitation(RainType.RAIN).biomeCategory(Category.PLAINS).downfall(0.3F).depth(0.0F).temperature(6).waterColor(BiomeHelper.RED_WATER_COLOR).waterFogColor(BiomeHelper.RED_WATER_COLOR).scale(0.0F).parent(null));
        DefaultBiomeFeatures.addSavannaExtraGrass(this);
        DefaultBiomeFeatures.addDefaultSprings(this);
        DefaultBiomeFeatures.addTaigaGrass(this);
        DefaultBiomeFeatures.addWarmFlowers(this);
        DefaultBiomeFeatures.addMossyStoneBlock(this);
        BiomeHelper.restoreVanillaStuff(this);
        BiomeHelper.addGallifreyTress(this);
        BiomeHelper.addGallifreyOres(this);
        BiomeHelper.addHut(this);
    }

    @Override
    public int getGrassColor(BlockPos pos) {
        return BiomeHelper.RED_GRASS_COLOR;
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        return BiomeHelper.GREY_LEAVES_COLOR;
    }
}
