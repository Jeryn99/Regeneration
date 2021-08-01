package me.suff.mc.regen.common.world.biome.surface;

import me.suff.mc.regen.util.RConstants;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/* Created by Craig on 19/03/2021 */
public class RSurfaceBuilder {

    public static void registerConfiguredSurfaceBuilders() {
        registerConfiguredSurfaceBuilder("death_zone", ConfiguredSurfaceBuilders.CONFIGURED_MOON_SURFACE_BUILDER);
    }

    private static <S extends SurfaceBuilder<?>> RegistryObject<S> createSurfaceBuilder(String name, Supplier<? extends S> surfaceBuilder) {
        return SurfaceBuilders.SURFACE_BUILDERS.register(name, surfaceBuilder);
    }

    private static <SC extends SurfaceBuilderConfiguration> ConfiguredSurfaceBuilder<SC> registerConfiguredSurfaceBuilder(String name, ConfiguredSurfaceBuilder<SC> configuredSurfaceBuilder) {
        return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(RConstants.MODID, name), configuredSurfaceBuilder);
    }

    public static class SurfaceBuilders {
        public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, RConstants.MODID);

        public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> DEATH_ZONE_BUILDER_INSTANCE = new DeathZoneSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC);
        public static final RegistryObject<SurfaceBuilder<SurfaceBuilderBaseConfiguration>> DEATH_ZONE_BUILDER = createSurfaceBuilder("death_zone", () -> DEATH_ZONE_BUILDER_INSTANCE);

    }

    public static class SurfaceBuilderConfigs {
        public static final SurfaceBuilderBaseConfiguration DEATH_ZONE_CONFIG = new SurfaceBuilderBaseConfiguration(Blocks.MYCELIUM.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.GRAVEL.defaultBlockState());
    }

    public static class ConfiguredSurfaceBuilders {
        public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> CONFIGURED_MOON_SURFACE_BUILDER = new ConfiguredSurfaceBuilder<>(SurfaceBuilders.DEATH_ZONE_BUILDER_INSTANCE, SurfaceBuilderConfigs.DEATH_ZONE_CONFIG);
    }


}

