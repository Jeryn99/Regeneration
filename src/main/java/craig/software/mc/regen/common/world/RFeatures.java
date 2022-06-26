package craig.software.mc.regen.common.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import craig.software.mc.regen.common.objects.RBlocks;
import craig.software.mc.regen.common.world.structures.TimelordSettlementHut;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber
public class RFeatures {

    public static final DeferredRegister<StructureType<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, RConstants.MODID);

    public static final RegistryObject<StructureType<?>> SETTLEMENT_HUT = DEFERRED_REGISTRY_STRUCTURE.register("timelord_settlement", () -> typeConvert(TimelordSettlementHut.CODEC));
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, RConstants.MODID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, RConstants.MODID);
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_ZINC_CONFIGURED = CONFIGURED_FEATURES.register("ore_zinc", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(ImmutableList.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, RBlocks.ZINC_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RBlocks.ZINC_ORE_DEEPSLATE.get().defaultBlockState())), 9)));
    public static RegistryObject<PlacedFeature> ORE_ZINC = PLACED_FEATURES.register("ore_zinc", () -> new PlacedFeature(Holder.direct(RFeatures.ORE_ZINC_CONFIGURED.get()), List.copyOf(commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))))));
    public static RegistryObject<ConfiguredFeature<?, ?>> ORE_ZINC_SMALL_CONFIGURED = CONFIGURED_FEATURES.register("ore_zinc_small", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(ImmutableList.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, RBlocks.ZINC_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, RBlocks.ZINC_ORE_DEEPSLATE.get().defaultBlockState())), 4)));
    public static RegistryObject<PlacedFeature> ORE_ZINC_SMALL = PLACED_FEATURES.register("ore_zinc_small", () -> new PlacedFeature(Holder.direct(RFeatures.ORE_ZINC_SMALL_CONFIGURED.get()), List.copyOf(commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))))));

    private static <S extends Structure> StructureType<S> typeConvert(Codec<S> codec) {
        return () -> codec;
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
