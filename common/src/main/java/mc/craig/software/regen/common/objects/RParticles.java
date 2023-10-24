package mc.craig.software.regen.common.objects;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class RParticles {

    public static final DeferredRegistry<ParticleType<?>> TYPES = DeferredRegistry.create(RConstants.MODID, Registries.PARTICLE_TYPE);

    public static final RegistrySupplier<SimpleParticleType> CONTAINER = TYPES.register("container", RParticles::getParticle);

    @ExpectPlatform
    private static SimpleParticleType getParticle() {
        throw new AssertionError();
    }
}
