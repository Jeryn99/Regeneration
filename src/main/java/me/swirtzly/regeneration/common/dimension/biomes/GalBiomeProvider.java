package me.swirtzly.regeneration.common.dimension.biomes;

import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.gen.layer.Layer;

public class GalBiomeProvider extends ModdedBiomeProvider {

    public GalBiomeProvider(OverworldBiomeProviderSettings settingsProvider) {
        super(settingsProvider);
    }

    public Layer wrapMappedBiomesAround(Layer parent) {
        GenLayerBiomeMapping mapping = new GenLayerBiomeMapping(RegenObjects.Biomes.REDLANDS.get(), parent);
        mapping.addBiomeOverride(Biomes.OCEAN, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.DEEP_OCEAN, RegenObjects.Biomes.GALLIFREYAN_OCEAN.get());
        mapping.addBiomeOverride(Biomes.OCEAN, RegenObjects.Biomes.GALLIFREYAN_OCEAN.get());
        mapping.addBiomeOverride(Biomes.BEACH, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.SNOWY_BEACH, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.RIVER, RegenObjects.Biomes.GALLIFREYAN_RIVER.get());
        mapping.addBiomeOverride(Biomes.SWAMP, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.FLOWER_FOREST, RegenObjects.Biomes.REDLANDS_FOREST.get());
        mapping.addBiomeOverride(Biomes.SWAMP_HILLS, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.SNOWY_MOUNTAINS, RegenObjects.Biomes.GALLIFREY_MOUNTAINS.get());
        mapping.addBiomeOverride(Biomes.MOUNTAINS, RegenObjects.Biomes.GALLIFREY_MOUNTAINS.get());
        mapping.addBiomeOverride(Biomes.MOUNTAIN_EDGE, RegenObjects.Biomes.REDLANDS_FOREST.get());
        mapping.addBiomeOverride(Biomes.JUNGLE_HILLS, RegenObjects.Biomes.GALLIFREY_MOUNTAINS.get());
        mapping.addBiomeOverride(Biomes.SNOWY_TAIGA_HILLS, RegenObjects.Biomes.GALLIFREY_MOUNTAINS.get());
        mapping.addBiomeOverride(Biomes.GRAVELLY_MOUNTAINS, RegenObjects.Biomes.GALLIFREY_MOUNTAINS.get());
        mapping.addBiomeOverride(Biomes.MODIFIED_GRAVELLY_MOUNTAINS, RegenObjects.Biomes.GALLIFREY_MOUNTAINS.get());
        mapping.addBiomeOverride(Biomes.TALL_BIRCH_FOREST, RegenObjects.Biomes.REDLANDS_FOREST.get());
        mapping.addBiomeOverride(Biomes.DESERT, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.DESERT_HILLS, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.BADLANDS, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.ERODED_BADLANDS, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.BADLANDS_PLATEAU, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.SNOWY_TUNDRA, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.SNOWY_MOUNTAINS, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.STONE_SHORE, RegenObjects.Biomes.WASTELANDS.get());
        mapping.addBiomeOverride(Biomes.FOREST, RegenObjects.Biomes.REDLANDS_FOREST.get());
        mapping.addBiomeOverride(Biomes.FLOWER_FOREST, RegenObjects.Biomes.REDLANDS_FOREST.get());
        return mapping;
    }

}