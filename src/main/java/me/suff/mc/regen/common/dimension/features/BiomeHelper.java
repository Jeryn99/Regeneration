package me.suff.mc.regen.common.dimension.features;

import me.suff.mc.regen.handlers.RegenObjects;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.HeightWithChanceConfig;
import net.minecraft.world.gen.placement.Placement;

/**
 * Created by Swirtzly
 * on 30/04/2020 @ 14:22
 */
public class BiomeHelper {

    public static final int RED_WATER_COLOR = 0xEB623D;
    public static final int GREY_LEAVES_COLOR = 0xEAEDED;
    public static final int RED_GRASS_COLOR = 0xE74C3C;
    public static final int WASTELAND_WATER = 0xF78F00;

    public static void restoreVanillaStuff(Biome biome) {
        biome.addStructureStart(Feature.MINESHAFT, new MineshaftConfig(0.004D, biome == RegenObjects.GallifreyBiomes.WASTELANDS.get() ? MineshaftStructure.Type.MESA : MineshaftStructure.Type.NORMAL));
        biome.addStructureStart(Feature.STRONGHOLD, IFeatureConfig.NONE);
        DefaultBiomeFeatures.addDefaultUndergroundVariety(biome);
        DefaultBiomeFeatures.addDefaultOres(biome);
        DefaultBiomeFeatures.addSurfaceFreezing(biome);
    }

    public static void addGallifreyTress(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.makeComposite(RegenObjects.WorldGenEntries.TREES, NoFeatureConfig.NONE, Placement.CHANCE_HEIGHTMAP, new ChanceConfig(32)));
    }

    public static void addHut(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.makeComposite(RegenObjects.WorldGenEntries.HUT, NoFeatureConfig.NONE, Placement.CHANCE_HEIGHTMAP, new ChanceConfig(32)));
    }

    public static void addBlackSpikes(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.makeComposite(RegenObjects.WorldGenEntries.SPIKEYS, NoFeatureConfig.NONE, Placement.CHANCE_HEIGHTMAP, new ChanceConfig(32)));
    }

    public static void addGallifreyOres(Biome biomeIn) {
        biomeIn.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.makeComposite(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, RegenObjects.Blocks.GAL_ORE.get().defaultBlockState(), 8), Placement.COUNT_RANGE, new CountRangeConfig(8, 0, 0, 16)));
    }

    public static void addSkulls(Biome biomeIn) {
        biomeIn.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.makeComposite(RegenObjects.WorldGenEntries.SKULLS, new ProbabilityConfig(0.01F), Placement.COUNT_CHANCE_HEIGHTMAP, new HeightWithChanceConfig(8, 0.125F)));
    }

}
