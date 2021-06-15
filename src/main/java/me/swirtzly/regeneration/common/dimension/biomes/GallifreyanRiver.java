package me.swirtzly.regeneration.common.dimension.biomes;

import me.swirtzly.regeneration.common.dimension.features.BiomeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SeaGrassConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import static me.swirtzly.regeneration.common.dimension.features.BiomeHelper.*;

public class GallifreyanRiver extends Biome {


    public GallifreyanRiver() {
        super((new Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.CONFIG_GRASS).precipitation(RainType.RAIN).biomeCategory(Category.RIVER).depth(-0.5F).scale(0.0F).temperature(6f).downfall(0.5F).waterColor(RED_WATER_COLOR).waterFogColor(RED_WATER_COLOR).parent(null));
        DefaultBiomeFeatures.addDefaultCarvers(this);
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
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, makeComposite(Feature.SEAGRASS, new SeaGrassConfig(48, 0.4D), Placement.TOP_SOLID_HEIGHTMAP, IPlacementConfig.NONE));
        DefaultBiomeFeatures.addSurfaceFreezing(this);
        BiomeHelper.addGallifreyOres(this);
        BiomeHelper.addHut(this);


    }

    @Override
    public int getGrassColor(BlockPos pos) {
        return RED_GRASS_COLOR;
    }

    @Override
    public int getFoliageColor(BlockPos pos) {
        return GREY_LEAVES_COLOR;
    }
}
