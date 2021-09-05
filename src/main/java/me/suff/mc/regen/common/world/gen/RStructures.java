package me.suff.mc.regen.common.world.gen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.suff.mc.regen.common.objects.RBlocks;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class RStructures {


    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, RConstants.MODID);
    public static final RegistryObject<Feature<OreConfiguration>> ZINC = FEATURES.register("zinc", () -> new OreFeature(OreConfiguration.CODEC));
    public static ConfiguredFeature<?, ?> GAl_ORE = null;

    public static void registerConfiguredFeatures() {
        final ImmutableList<OreConfiguration.TargetBlockState> ZINC_TARGET = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.STONE_ORE_REPLACEABLES, RBlocks.ZINC_ORE.get().defaultBlockState()), OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, RBlocks.ZINC_ORE.get().defaultBlockState()));
        GAl_ORE = ZINC.get().configured(new OreConfiguration(ZINC_TARGET, 9)).count(2).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(63)).squared().count(20);
        registerConfiguredFeature("zinc", GAl_ORE);
    }

    private static <T extends Feature<?>> void registerConfiguredFeature(String registryName, ConfiguredFeature<?, ?> configuredFeature) {
        Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;
        Registry.register(registry, new ResourceLocation(RConstants.MODID, registryName), configuredFeature);
    }

    /**
     * Setup the structure and add the rarity settings.
     * <br> Call this in CommonSetup in a deferred work task to reduce concurrent modification issues as we are modifying multiple maps we ATed
     */
    public static void setupStructures() {
        setupStructure(Structures.HUTS.get(), new StructureFeatureConfiguration(400, 140, 1234567890), true); //Maximum of 200 chunks apart, minimum 100 chunks apart, chunk seed respectively
    }

    private static <T extends StructureFeature<?>> void registerConfiguredStructure(String registryName, Supplier<T> structure, ConfiguredStructureFeature<?, ?> configuredStructure) {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(RConstants.MODID, registryName), configuredStructure);
        FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(structure.get(), configuredStructure);
    }

    private static <T extends StructureFeature<?>> RegistryObject<T> setupStructure(String name, Supplier<T> structure) {
        return Structures.STRUCTURES.register(name, structure);
    }

    /**
     * Add Structure to the structure registry map and setup the seperation settings.
     */
    public static <F extends StructureFeature<?>> void setupStructure(F structure, StructureFeatureConfiguration structureSeparationSettings, boolean transformSurroundingLand) {
        /*
         * We need to add our structures into the map in Structure alongside vanilla
         * structures or else it will cause errors. Called by registerStructure.
         *
         * If the registration is setup properly for the structure, getRegistryName() should never return null.
         */
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
        /*
         * Will add land at the base of the structure like it does for Villages and Outposts.
         * Doesn't work well on structures that have pieces stacked vertically or change in heights.
         */
        if (transformSurroundingLand) {
            StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder().addAll(StructureFeature.NOISE_AFFECTING_FEATURES).add(structure).build();
        }
        /*
         * Adds the structure's spacing into several places so that the structure's spacing remains
         * correct in any dimension or worldtype instead of not spawning.
         *
         * However, it seems it doesn't always work for code made dimensions as they read from
         * this list beforehand. Use the WorldEvent.Load event to add
         * the structure spacing from this list into that dimension.
         */
        StructureSettings.DEFAULTS =
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                        .putAll(StructureSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();
    }

    /**
     * Register the pieces of your structure if this has not been done by a jigsaw pool.
     * <br> You MUST call this method to allow the chunk to save.
     * <br> Otherwise the chunk won't save and complain it's missing a registry id for the structure piece. Darn vanilla...
     */
    public static StructurePieceType registerStructurePiece(StructurePieceType type, String key) {
        return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(RConstants.MODID, key), type);
    }

    public static class Structures {
        public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, RConstants.MODID);

        /**
         * The Structure registry object. This isn't actually setup yet, see
         */
        public static final RegistryObject<StructureFeature<ProbabilityFeatureConfiguration>> HUTS = setupStructure("hut", () -> new GallifreyanHuts(ProbabilityFeatureConfiguration.CODEC));
        /**
         * Static instance of our structure so we can reference it before registry stuff happens and use it to make configured structures in ConfiguredStructures
         */
        public static StructurePieceType HUT_PIECE = registerStructurePiece(HutPieces.Piece::new, "hut_piece");

    }

    /**
     * Configure the structure so it can be placed in the world. <br> Register Configured Structures in Common Setup. There is currently no Forge Registry for configured structures because configure structures are a dynamic registry and can cause issues if it were a Forge registry.
     */
    public static class ConfiguredStructures {
        /**
         * Static instance of our configured structure feature so we can reference it for registration
         */
        public static ConfiguredStructureFeature<?, ?> CONFIGURED_HUTS = Structures.HUTS.get().configured(new ProbabilityFeatureConfiguration(1));

        public static void registerConfiguredStructures() {
            registerConfiguredStructure("configured_huts", Structures.HUTS, CONFIGURED_HUTS); //We have to add this to flatGeneratorSettings to account for mods that add custom chunk generators or superflat world type
        }
    }


    /*===Structure Registration End===*/


}
