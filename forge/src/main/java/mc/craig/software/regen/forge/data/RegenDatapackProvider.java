package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.forge.data.level.RegenBiomeModifiers;
import mc.craig.software.regen.forge.data.level.RegenConfiguredFeatures;
import mc.craig.software.regen.forge.data.level.RegenPlacedFeatures;
import mc.craig.software.regen.util.RegenDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegenDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, RegenConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, RegenPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, RegenBiomeModifiers::bootstrap)
            .add(Registries.DAMAGE_TYPE, RegenDamageTypes::bootstrap);

    public RegenDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BUILDER, Set.of(Regeneration.MOD_ID));
    }

    public static HolderLookup.Provider createLookup() {
        return BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), VanillaRegistries.createLookup());
    }

}
