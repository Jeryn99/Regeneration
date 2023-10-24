package mc.craig.software.regen.forge.data.level;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class RegenBiomeModifiers {
    private static final ResourceKey<BiomeModifier> ADD_OVERWORLD_FEATURES = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Regeneration.MOD_ID, "add_overworld_features"));

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var overworldTag = context.lookup(Registries.BIOME).getOrThrow(RegenUtil.IS_OVERWORLD);
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);

        ForgeBiomeModifiers.AddFeaturesBiomeModifier oreModifer = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(overworldTag, HolderSet.direct(placed.getOrThrow(RegenPlacedFeatures.ORE_ZINC), placed.getOrThrow(RegenPlacedFeatures.ORE_ZINC_SMALL)), GenerationStep.Decoration.UNDERGROUND_ORES);

        context.register(ADD_OVERWORLD_FEATURES, oreModifer);
    }
}
