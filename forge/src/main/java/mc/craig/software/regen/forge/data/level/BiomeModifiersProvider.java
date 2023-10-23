package mc.craig.software.regen.forge.data.level;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.world.RFeatures;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeModifiersProvider {
    private static final ResourceKey<BiomeModifier> ADD_OVERWORLD_FEATURES = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Regeneration.MOD_ID, "add_overworld_features"));
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var overworldTag = context.lookup(Registries.BIOME).getOrThrow(RegenUtil.IS_OVERWORLD);

        ForgeBiomeModifiers.AddFeaturesBiomeModifier oreModifer = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(overworldTag, HolderSet.direct(Holder.direct(RFeatures.ORE_ZINC.get())), GenerationStep.Decoration.UNDERGROUND_ORES);
        ForgeBiomeModifiers.AddFeaturesBiomeModifier oreModiferSmall = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(overworldTag, HolderSet.direct(Holder.direct(RFeatures.ORE_ZINC_SMALL.get())), GenerationStep.Decoration.UNDERGROUND_ORES);

        context.register(ADD_OVERWORLD_FEATURES, oreModifer);
        context.register(ADD_OVERWORLD_FEATURES, oreModiferSmall);
    }
}
