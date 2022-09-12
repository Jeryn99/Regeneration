package mc.craig.software.regen.common.objects.fabric;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class RParticlesImpl {
    public static SimpleParticleType getParticle() {
        return FabricParticleTypes.simple();
    }
}
