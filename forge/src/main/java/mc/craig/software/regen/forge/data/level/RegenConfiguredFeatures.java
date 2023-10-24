package mc.craig.software.regen.forge.data.level;

import com.google.common.collect.ImmutableList;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.objects.RBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class RegenConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ZINC = createKey("ore_zinc");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ZINC_SMALL = createKey("ore_zinc_small");

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Regeneration.MOD_ID, name));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {

        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(context, ORE_ZINC, Feature.ORE,
                new OreConfiguration(
                        ImmutableList.of(
                                OreConfiguration.target(stoneReplaceable, RBlocks.ZINC_ORE.get().defaultBlockState()),
                                OreConfiguration.target(deepslateReplaceable, RBlocks.ZINC_ORE_DEEPSLATE.get().defaultBlockState())), 9));
        register(context, ORE_ZINC_SMALL, Feature.ORE,
                new OreConfiguration(
                        ImmutableList.of(
                                OreConfiguration.target(stoneReplaceable, RBlocks.ZINC_ORE.get().defaultBlockState()),
                                OreConfiguration.target(deepslateReplaceable, RBlocks.ZINC_ORE_DEEPSLATE.get().defaultBlockState())), 4));
    }

    public static void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, Feature<NoneFeatureConfiguration> feature) {
        register(context, key, feature, FeatureConfiguration.NONE);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
