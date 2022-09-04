package mc.craig.software.regen.common.objects;

import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistrySupplier;

/* Created by Craig on 09/03/2021 */
public class RParticles {

    public static final DeferredRegistry<ParticleType<?>> TYPES = DeferredRegistry.create(RConstants.MODID, Registry.PARTICLE_TYPE_REGISTRY);

    public static final RegistrySupplier<SimpleParticleType> CONTAINER = TYPES.register("container", () -> new SimpleParticleType(false));


}
