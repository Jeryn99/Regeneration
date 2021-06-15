package me.suff.mc.regen.common.dimension.biomes;

import me.suff.mc.regen.common.dimension.features.BiomeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SeaGrassConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public final class GallifreyanOcean extends Biome {
    public GallifreyanOcean() {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.CONFIG_GRASS).precipitation(Biome.RainType.RAIN).biomeCategory(Biome.Category.OCEAN).depth(-1.0F).scale(0.1F).temperature(0.5F).downfall(0.5F).waterColor(BiomeHelper.RED_WATER_COLOR).waterFogColor(BiomeHelper.RED_WATER_COLOR).parent(null));
        DefaultBiomeFeatures.addOceanCarvers(this);
        DefaultBiomeFeatures.addStructureFeaturePlacement(this);
        DefaultBiomeFeatures.addDefaultLakes(this);
        DefaultBiomeFeatures.addDefaultMonsterRoom(this);
        DefaultBiomeFeatures.addDefaultUndergroundVariety(this);
        DefaultBiomeFeatures.addDefaultOres(this);
        DefaultBiomeFeatures.addDefaultSoftDisks(this);
        DefaultBiomeFeatures.addWaterTrees(this);
        DefaultBiomeFeatures.addDefaultFlowers(this);
        DefaultBiomeFeatures.addDefaultGrass(this);
        DefaultBiomeFeatures.addDefaultMushrooms(this);
        DefaultBiomeFeatures.addDefaultExtraVegetation(this);
        DefaultBiomeFeatures.addDefaultSprings(this);
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, makeComposite(Feature.SEAGRASS, new SeaGrassConfig(48, 0.3D), Placement.TOP_SOLID_HEIGHTMAP, IPlacementConfig.NONE));
        DefaultBiomeFeatures.addDefaultSeagrass(this);
        DefaultBiomeFeatures.addColdOceanExtraVegetation(this);
        DefaultBiomeFeatures.addSurfaceFreezing(this);
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