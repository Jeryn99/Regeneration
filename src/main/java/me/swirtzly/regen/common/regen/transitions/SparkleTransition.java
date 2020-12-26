package me.swirtzly.regen.common.regen.transitions;

import me.swirtzly.regen.client.rendering.transitions.SparkleTransitionRenderer;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.POVMessage;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

public class SparkleTransition implements TransitionType<SparkleTransitionRenderer> {

    @Override
    public int getAnimationLength() {
        return 230;
    }

    @Override
    public SparkleTransitionRenderer getRenderer() {
        return SparkleTransitionRenderer.INSTANCE;
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

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(RConstants.MODID, "sparkle");
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        LivingEntity living = cap.getLiving();
        if (living instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
        }
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
        }
    }

}
