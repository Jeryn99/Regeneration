package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.POVMessage;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.network.PacketDistributor;

public class EnderDragonTransition extends TransitionType {
    @Override
    public int getAnimationLength() {
        return 300;
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {

        if (!cap.getLiving().level.isClientSide) {
            if (cap.getLiving() instanceof ServerPlayer) {
                NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
            }
        }

        if (cap.getLiving() instanceof Player) {
            Player serverPlayerEntity = (Player) cap.getLiving();
            serverPlayerEntity.abilities.mayfly = RegenConfig.COMMON.allowUpwardsMotion.get();
            serverPlayerEntity.abilities.flying = RegenConfig.COMMON.allowUpwardsMotion.get();
        }
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayer) {
            ServerPlayer serverPlayerEntity = (ServerPlayer) cap.getLiving();
            serverPlayerEntity.abilities.mayfly = serverPlayerEntity.isCreative();
            serverPlayerEntity.abilities.flying = false;

            if (cap.getLiving() instanceof ServerPlayer) {
                NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) cap.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
            }
        }

    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{SoundEvents.ENDER_DRAGON_DEATH};
    }

    @Override
    public Vec3 getDefaultPrimaryColor() {
        return Vec3.ZERO;
    }

    @Override
    public Vec3 getDefaultSecondaryColor() {
        return Vec3.ZERO;
    }

}
