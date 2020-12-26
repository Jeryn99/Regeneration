package me.swirtzly.regen.common.regen.transitions;

import me.swirtzly.regen.client.rendering.transitions.EnderDragonTransitionRenderer;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.POVMessage;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

public class EnderDragonTransition implements TransitionType<EnderDragonTransitionRenderer> {
    @Override
    public int getAnimationLength() {
        return 300;
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {

        if (!cap.getLiving().world.isRemote) {
            if (cap.getLiving() instanceof ServerPlayerEntity) {
                NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
            }
        }

        if (cap.getLiving() instanceof PlayerEntity) {
            PlayerEntity serverPlayerEntity = (PlayerEntity) cap.getLiving();
            serverPlayerEntity.abilities.allowFlying = RegenConfig.COMMON.allowUpwardsMotion.get();
            serverPlayerEntity.abilities.isFlying = RegenConfig.COMMON.allowUpwardsMotion.get();
        } else {
            if (cap.getLiving().getPosition().getY() <= 100 && RegenConfig.COMMON.allowUpwardsMotion.get()) {
                BlockPos upwards = cap.getLiving().getPosition().up(2);
                BlockPos pos = upwards.subtract(cap.getLiving().getPosition());
                Vector3d vec = new Vector3d(pos.getX(), pos.getY(), pos.getZ()).normalize();
                cap.getLiving().setMotion(cap.getLiving().getMotion().add(vec.scale(0.10D)));
            }
        }
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) cap.getLiving();
            serverPlayerEntity.abilities.allowFlying = serverPlayerEntity.isCreative();
            serverPlayerEntity.abilities.isFlying = false;

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
        return new SoundEvent[]{SoundEvents.ENTITY_ENDER_DRAGON_DEATH};
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
