package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.POVMessage;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.network.PacketDistributor;

public final class TroughtonTransition extends TransitionType {

    @Override
    public int getAnimationLength() {
        return 330;
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayer) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
        }
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        LivingEntity living = cap.getLiving();
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayer) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) cap.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
        }
        cap.setUpdateTicks(0);
        cap.syncToClients(null);
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.REGENERATION_TROUGHTON.get()};
    }

    @Override
    public Vec3 getDefaultPrimaryColor() {
        return new Vec3(0.5, 0.5, 0.5);
    }

    @Override
    public Vec3 getDefaultSecondaryColor() {
        return new Vec3(0.5, 0.5, 0.5);
    }

}
