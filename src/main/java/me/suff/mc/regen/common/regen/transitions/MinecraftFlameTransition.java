package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.common.objects.RParticles;
import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class MinecraftFlameTransition extends FieryTransition {

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        super.onUpdateMidRegen(cap);
        if (cap.getLiving().level.isClientSide) return;
        ServerWorld serverWorld = (ServerWorld) cap.getLiving().level;
        BlockPos blockPos = cap.getLiving().blockPosition();
        serverWorld.sendParticles(RParticles.CONTAINER.get(), blockPos.getX(), (double) blockPos.getY() + 1D, blockPos.getZ(), 8, 0.5D, 0.25D, 0.5D, 0.0D);
    }
}
