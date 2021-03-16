package me.suff.mc.regen.common.world.gen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.suff.mc.regen.common.objects.RBlocks;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class RStructures {


    public static final DeferredRegister< Feature< ? > > FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, RConstants.MODID);
    public static final RegistryObject< Feature< OreFeatureConfig > > ZINC = FEATURES.register("zinc", () -> new OreFeature(OreFeatureConfig.CODEC));

    public static ConfiguredFeature< ?, ? > GAl_ORE = null;

    public static void registerConfiguredFeatures() {

        GAl_ORE = ZINC.get().configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, RBlocks.ZINC_ORE.get().defaultBlockState(), 9)).range(32).squared().chance(2);
        registerConfiguredFeature("zinc", GAl_ORE);
    }

    private static < T extends Feature< ? > > void registerConfiguredFeature(String registryName, ConfiguredFeature< ?, ? > configuredFeature) {
        Registry< ConfiguredFeature< ?, ? > > registry = WorldGenRegistries.CONFIGURED_FEATURE;
        Registry.register(registry, new ResourceLocation(RConstants.MODID, registryName), configuredFeature);
    }

    /**
     * Setup the structure and add the rarity settings.
     * <br> Call this in CommonSetup in a deferred work task to reduce concurrent modification issues as we are modifying multiple maps we ATed
     */
    public static void setupStructures() {
        setupStructure(Structures.HUTS.get(), new StructureSeparationSettings(400, 140, 1234567890), true); //Maximum of 200 chunks apart, minimum 100 chunks apart, chunk seed respectively
    }

    private static < T extends Structure< ? > > void registerConfiguredStructure(String registryName, Supplier< T > structure, StructureFeature< ?, ? > configuredStructure) {
        Registry< StructureFeature< ?, ? > > registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(RConstants.MODID, registryName), configuredStructure);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(structure.get(), configuredStructure);
    }

    private static < T extends Structure< ? > > RegistryObject< T > setupStructure(String name, Supplier< T > structure) {
        return Structures.STRUCTURES.register(name, structure);
    }

    /**
     * Add Structure to the structure registry map and setup the seperation settings.
     */
    public static < F extends Structure< ? > > void setupStructure(F structure, StructureSeparationSettings structureSeparationSettings, boolean transformSurroundingLand) {
        /*
         * We need to add our structures into the map in Structure alongside vanilla
         * structures or else it will cause errors. Called by registerStructure.
         *
         * If the registration is setup properly for the structure, getRegistryName() should never return null.
         */
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
        /*
         * Will add land at the base of the structure like it does for Villages and Outposts.
         * Doesn't work well on structures that have pieces stacked vertically or change in heights.
         */
        if (transformSurroundingLand) {
            Structure.NOISE_AFFECTING_FEATURES = ImmutableList.< Structure< ? > >builder().addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure).build();
        }
        /*
         * Adds the structure's spacing into several places so that the structure's spacing remains
         * correct in any dimension or worldtype instead of not spawning.
         *
         * However, it seems it doesn't always work for code made dimensions as they read from
         * this list beforehand. Use the WorldEvent.Load event to add
         * the structure spacing from this list into that dimension.
         */
        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.< Structure< ? >, StructureSeparationSettings >builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();
    }

    /**
     * Register the pieces of your structure if this has not been done by a jigsaw pool.
     * <br> You MUST call this method to allow the chunk to save.
     * <br> Otherwise the chunk won't save and complain it's missing a registry id for the structure piece. Darn vanilla...
     */
    public static IStructurePieceType registerStructurePiece(IStructurePieceType type, String key) {
        return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(RConstants.MODID, key), type);
    }

    public static class Structures {
        public static final DeferredRegister< Structure< ? > > STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, RConstants.MODID);

        /**
         * The Structure registry object. This isn't actually setup yet, see {@link RStructures#setupStructure(Structure, StructureSeparationSettings, boolean)}
         */
        public static final RegistryObject< Structure< ProbabilityConfig > > HUTS = setupStructure("hut", () -> new GallifreyanHuts(ProbabilityConfig.CODEC));
        /**
         * Static instance of our structure so we can reference it before registry stuff happens and use it to make configured structures in ConfiguredStructures
         */
        public static IStructurePieceType HUT_PIECE = registerStructurePiece(HutPieces.Piece::new, "hut_piece");

    }

    /**
     * Configure the structure so it can be placed in the world. <br> Register Configured Structures in Common Setup. There is currently no Forge Registry for configured structures because configure structures are a dynamic registry and can cause issues if it were a Forge registry.
     */
    public static class ConfiguredStructures {
        /**
         * Static instance of our configured structure feature so we can reference it for registration
         */
        public static StructureFeature< ?, ? > CONFIGURED_HUTS = Structures.HUTS.get().configured(new ProbabilityConfig(1));

        public static void registerConfiguredStructures() {
            registerConfiguredStructure("configured_huts", Structures.HUTS, CONFIGURED_HUTS); //We have to add this to flatGeneratorSettings to account for mods that add custom chunk generators or superflat world type
        }
    }


    /**===Structure Registration End===*/


}
