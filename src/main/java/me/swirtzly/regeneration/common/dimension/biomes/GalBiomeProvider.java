package me.swirtzly.regeneration.common.dimension.biomes;

import me.swirtzly.regeneration.common.dimension.RBiomes;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.gen.layer.Layer;

public class GalBiomeProvider extends ModdedBiomeProvider {

    public GalBiomeProvider(OverworldBiomeProviderSettings settingsProvider) {
        super(settingsProvider);
    }

    public Layer wrapMappedBiomesAround(Layer parent) {
        GenLayerBiomeMapping mapping = new GenLayerBiomeMapping(RBiomes.redLands, parent);
        mapping.addBiomeOverride(Biomes.OCEAN, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.DEEP_OCEAN, Biomes.OCEAN);
        mapping.addBiomeOverride(Biomes.BEACH, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.SNOWY_BEACH, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.RIVER, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.SWAMP, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.FLOWER_FOREST, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.SWAMP_HILLS, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.SNOWY_MOUNTAINS, RBiomes.gallifreyMountains);
        mapping.addBiomeOverride(Biomes.MOUNTAINS, RBiomes.gallifreyMountains);
        mapping.addBiomeOverride(Biomes.MOUNTAIN_EDGE, RBiomes.gallifreyMountains);
        mapping.addBiomeOverride(Biomes.JUNGLE_HILLS, RBiomes.gallifreyMountains);
        mapping.addBiomeOverride(Biomes.SNOWY_TAIGA_HILLS, RBiomes.gallifreyMountains);
        mapping.addBiomeOverride(Biomes.GRAVELLY_MOUNTAINS, RBiomes.gallifreyMountains);
        mapping.addBiomeOverride(Biomes.MODIFIED_GRAVELLY_MOUNTAINS, RBiomes.gallifreyMountains);
        mapping.addBiomeOverride(Biomes.TALL_BIRCH_FOREST, Biomes.FOREST);
        mapping.addBiomeOverride(Biomes.DESERT, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.DESERT_HILLS, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.BADLANDS, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.ERODED_BADLANDS, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.BADLANDS_PLATEAU, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.SNOWY_TUNDRA, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.SNOWY_MOUNTAINS, RBiomes.wasteLands);
        mapping.addBiomeOverride(Biomes.STONE_SHORE, RBiomes.wasteLands);
        return mapping;
    }

}