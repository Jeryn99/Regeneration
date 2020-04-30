package me.swirtzly.regeneration.common.dimension.features;

import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.PillagerOutpostConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

/**
 * Created by Swirtzly
 * on 30/04/2020 @ 14:22
 */
public class BiomeHelper {

    public static void restoreVanillaStuff(Biome biome) {
        biome.addStructure(Feature.VILLAGE, new VillageConfig("village/plains/town_centers", 6));
        biome.addStructure(Feature.PILLAGER_OUTPOST, new PillagerOutpostConfig(0.004D));
        biome.addStructure(Feature.MINESHAFT, new MineshaftConfig(0.004D, biome == RegenObjects.Biomes.WASTELANDS.get() ? MineshaftStructure.Type.MESA : MineshaftStructure.Type.NORMAL));
        biome.addStructure(Feature.STRONGHOLD, IFeatureConfig.NO_FEATURE_CONFIG);
        DefaultBiomeFeatures.addStoneVariants(biome);
        DefaultBiomeFeatures.addOres(biome);
        DefaultBiomeFeatures.addFreezeTopLayer(biome);
    }

    public static void addGallifreyTress(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(RegenObjects.TREES, NoFeatureConfig.NO_FEATURE_CONFIG, Placement.CHANCE_HEIGHTMAP, new ChanceConfig(32)));
    }

}
