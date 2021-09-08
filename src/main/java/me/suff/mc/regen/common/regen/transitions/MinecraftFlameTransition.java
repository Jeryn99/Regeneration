package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.common.objects.RParticles;
import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.server.ServerWorld;

public class MinecraftFlameTransition extends FieryTransition {

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        super.onUpdateMidRegen(cap);
        if (cap.getLiving().level.isClientSide) return;
        ServerWorld serverWorld = (ServerWorld) cap.getLiving().level;
        LivingEntity living = cap.getLiving();
        serverWorld.sendParticles(RParticles.CONTAINER.get(), living.getRandomX(0.5D), living.getRandomY(), living.getRandomZ(0.5D), 8, 0.5D, 0.25D, 0.5D, 0.0D);
    }
}
