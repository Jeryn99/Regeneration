package me.suff.mc.regen.common.world;

import me.suff.mc.regen.common.objects.RBlocks;
import me.suff.mc.regen.common.world.structures.TimelordSettlementHut;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.List;

@Mod.EventBusSubscriber
public class RFeatures {

    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, RConstants.MODID);

    public static final RegistryObject<StructureFeature<?>> SETTLEMENT_HUT = DEFERRED_REGISTRY_STRUCTURE.register("timelord_settlement", () -> new TimelordSettlementHut(NoneFeatureConfiguration.CODEC));

    private static final HashSet<Holder<PlacedFeature>> ORES = new HashSet<>();


    public static void ores() {
        BlockState blockState = RBlocks.ZINC_ORE.get().defaultBlockState();
        BlockState blockStateDeep = RBlocks.ZINC_ORE_DEEPSLATE.get().defaultBlockState();

        List<OreConfiguration.TargetBlockState> targetBlockStateList = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, blockState), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, blockStateDeep));
        Holder<ConfiguredFeature<OreConfiguration, ?>> feature = FeatureUtils.register("kontron_ore", Feature.ORE, new OreConfiguration(targetBlockStateList, 9));
        Holder<PlacedFeature> placedFeatureUpper = PlacementUtils.register("kontron_ore_upper", feature, commonOrePlacement(90, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))));
        Holder<PlacedFeature> placedFeatureMiddle = PlacementUtils.register("kontron_ore_middle", feature, commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));
        ORES.add(placedFeatureUpper);
        ORES.add(placedFeatureMiddle);
    }

    @SubscribeEvent
    public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();
        if (event.getCategory() != Biome.BiomeCategory.NETHER && event.getCategory() != Biome.BiomeCategory.THEEND) {
            for (Holder<PlacedFeature> feature : ORES) {
                gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature);
            }
        }
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    private static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }


}
