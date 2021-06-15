package me.suff.mc.regen.common.world.biome.surface;

import me.suff.mc.regen.util.RConstants;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
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

    private static <SC extends ISurfaceBuilderConfig> ConfiguredSurfaceBuilder<SC> registerConfiguredSurfaceBuilder(String name, ConfiguredSurfaceBuilder<SC> configuredSurfaceBuilder) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(RConstants.MODID, name), configuredSurfaceBuilder);
    }

    public static class SurfaceBuilders {
        public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, RConstants.MODID);

        public static final SurfaceBuilder<SurfaceBuilderConfig> DEATH_ZONE_BUILDER_INSTANCE = new DeathZoneSurfaceBuilder(SurfaceBuilderConfig.CODEC);
        public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> DEATH_ZONE_BUILDER = createSurfaceBuilder("death_zone", () -> DEATH_ZONE_BUILDER_INSTANCE);

    }

    public static class SurfaceBuilderConfigs {
        public static final SurfaceBuilderConfig DEATH_ZONE_CONFIG = new SurfaceBuilderConfig(Blocks.MYCELIUM.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.GRAVEL.defaultBlockState());
    }

    public static class ConfiguredSurfaceBuilders {
        public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> CONFIGURED_MOON_SURFACE_BUILDER = new ConfiguredSurfaceBuilder<>(SurfaceBuilders.DEATH_ZONE_BUILDER_INSTANCE, SurfaceBuilderConfigs.DEATH_ZONE_CONFIG);
    }


}

