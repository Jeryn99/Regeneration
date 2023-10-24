package mc.craig.software.regen.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.commands.arguments.TraitArgumentType;
import mc.craig.software.regen.common.entities.Timelord;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.world.structures.pieces.StructurePieces;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.fabric.handlers.CommonEvents;
import mc.craig.software.regen.util.constants.RConstants;
import mc.craig.software.regen.util.fabric.PlatformImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.fml.config.ModConfig;

public class RegenerationFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Regeneration.init();
        ForgeConfigRegistry.INSTANCE.register(Regeneration.MOD_ID, ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
        ForgeConfigRegistry.INSTANCE.register(Regeneration.MOD_ID, ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);
        PlatformImpl.init();
        CommonEvents.init();
        StructurePieces.init();
        levelManipulation();
        ArgumentTypeRegistry.registerArgumentType(new ResourceLocation(RConstants.MODID, "traits"), TraitArgumentType.class, SingletonArgumentInfo.contextFree(TraitArgumentType::traitArgumentType));

        FabricDefaultAttributeRegistry.register(REntities.TIMELORD.get(), Timelord.createAttributes());

    }

    private void levelManipulation() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(RConstants.MODID, "ore_zinc")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(RConstants.MODID, "ore_zinc_small")));
    }
}
