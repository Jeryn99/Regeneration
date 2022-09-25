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

/* Created by Craig on 31/01/2021 */
public class BlazeTranstion extends TransitionType {
    @Override
    public int getAnimationLength() {
        return 150;
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        LivingEntity entity = cap.getLiving();
        if (!cap.getLiving().level.isClientSide) {
            if (cap.getLiving() instanceof ServerPlayerEntity) {
                NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
            }
        }
    }

    @Override
    public void onFinishRegeneration(IRegen capability) {
        if (capability.getLiving() instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) capability.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
        }
        capability.setUpdateTicks(0);
        capability.syncToClients(null);
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.REGENERATION_0.get(), RSounds.REGENERATION_1.get(), RSounds.REGENERATION_2.get(), RSounds.REGENERATION_3.get(), RSounds.REGENERATION_4.get(), RSounds.REGENERATION_5.get(), RSounds.REGENERATION_6.get(), RSounds.REGENERATION_7.get()};
    }

    @Override
    public Vector3d getDefaultPrimaryColor() {
        return new Vector3d(0.93F, 0.61F, 0F);
    }

    @Override
    public Vector3d getDefaultSecondaryColor() {
        return new Vector3d(1F, 0.5F, 0.18F);
    }
}
