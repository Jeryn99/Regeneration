package me.suff.mc.regen.common.world;

import com.mojang.serialization.Codec;
import me.suff.mc.regen.common.objects.RBlocks;
import me.suff.mc.regen.common.world.structures.TimelordSettlementHut;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.state.BlockState;
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

import java.util.HashSet;
import java.util.List;

@Mod.EventBusSubscriber
public class RFeatures {

    public static final DeferredRegister<StructureType<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, RConstants.MODID);

    public static final RegistryObject<StructureType<?>> SETTLEMENT_HUT = DEFERRED_REGISTRY_STRUCTURE.register("timelord_settlement", () -> typeConvert(TimelordSettlementHut.CODEC));

    private static <S extends Structure> StructureType<S> typeConvert(Codec<S> codec) {
        return () -> codec;
    }

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
