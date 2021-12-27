package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.util.RConstants;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/* Created by Craig on 09/03/2021 */
public class RParticles {

    public static final DeferredRegister<ParticleType<?>> TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RConstants.MODID);

    public static final RegistryObject<SimpleParticleType> CONTAINER = TYPES.register("container", () -> new SimpleParticleType(false));


}
