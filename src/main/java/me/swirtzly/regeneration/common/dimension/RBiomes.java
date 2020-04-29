package me.swirtzly.regeneration.common.dimension;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.IChunkGeneratorFactory;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Swirtzly
 * on 28/04/2020 @ 11:13
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RBiomes {
    public static final ChunkGeneratorType<OverworldGenSettings, GallifreyChunkGenerator> GALLIFREY;
    public static Biome gallifreyMountains;
    public static Biome redLands;
    public static Biome redLandsForest;
    public static Biome wasteLands;
    public static Biome gallifreyRiver;
    public static Biome gallifreyOcean;

    static {
        IChunkGeneratorFactory<OverworldGenSettings, GallifreyChunkGenerator> factoryIn3 = GallifreyChunkGenerator::new;
        GALLIFREY = new ChunkGeneratorType<>(factoryIn3, true, OverworldGenSettings::new);
        GALLIFREY.setRegistryName("regeneration", "gallifrey_chunks");
    }

    public static void registerBiome() {
        addTypes(gallifreyMountains);
        addTypes(redLands);
        addTypes(wasteLands);
        addTypes(redLandsForest);
        addTypes(gallifreyRiver);
    }

    public static void addTypes(Biome biome) {
        BiomeDictionary.addTypes(biome, BiomeDictionary.Type.MOUNTAIN);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<ChunkGeneratorType<?, ?>> event) {
        event.getRegistry().registerAll(GALLIFREY);
    }


}
