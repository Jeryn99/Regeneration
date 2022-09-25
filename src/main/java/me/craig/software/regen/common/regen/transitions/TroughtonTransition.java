package me.craig.software.regen.common.regen.transitions;

import me.craig.software.regen.common.objects.RSounds;
import me.craig.software.regen.network.NetworkDispatcher;
import me.craig.software.regen.network.messages.POVMessage;
import me.craig.software.regen.util.RConstants;
import me.craig.software.regen.common.regen.IRegen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

public final class TroughtonTransition extends TransitionType {

    @Override
    public int getAnimationLength() {
        return 330;
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
        }
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        LivingEntity living = cap.getLiving();
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
        }
        cap.setUpdateTicks(0);
        cap.syncToClients(null);
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.REGENERATION_TROUGHTON.get()};
    }

    @Override
    public Vector3d getDefaultPrimaryColor() {
        return new Vector3d(0.5, 0.5, 0.5);
    }

    @Override
    public Vector3d getDefaultSecondaryColor() {
        return new Vector3d(0.5, 0.5, 0.5);
    }

}
