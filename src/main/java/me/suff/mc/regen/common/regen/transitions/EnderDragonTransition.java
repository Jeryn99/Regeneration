package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.client.rendering.transitions.EnderDragonTransitionRenderer;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.POVMessage;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

public class EnderDragonTransition implements TransitionType< EnderDragonTransitionRenderer > {
    @Override
    public int getAnimationLength() {
        return 300;
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {

        if (!cap.getLiving().level.isClientSide) {
            if (cap.getLiving() instanceof ServerPlayerEntity) {
                NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
            }
        }

        if (cap.getLiving() instanceof PlayerEntity) {
            PlayerEntity serverPlayerEntity = (PlayerEntity) cap.getLiving();
            serverPlayerEntity.abilities.mayfly = RegenConfig.COMMON.allowUpwardsMotion.get();
            serverPlayerEntity.abilities.flying = RegenConfig.COMMON.allowUpwardsMotion.get();
        }
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) cap.getLiving();
            serverPlayerEntity.abilities.mayfly = serverPlayerEntity.isCreative();
            serverPlayerEntity.abilities.flying = false;

            if (cap.getLiving() instanceof ServerPlayerEntity) {
                NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
            }
        }

    }

    @Override
    public EnderDragonTransitionRenderer getRenderer() {
        return EnderDragonTransitionRenderer.INSTANCE;
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{SoundEvents.ENDER_DRAGON_DEATH};
    }

    @Override
    public Vector3d getDefaultPrimaryColor() {
        return Vector3d.ZERO;
    }

    @Override
    public Vector3d getDefaultSecondaryColor() {
        return Vector3d.ZERO;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(RConstants.MODID, "ender_dragon");
    }
}
