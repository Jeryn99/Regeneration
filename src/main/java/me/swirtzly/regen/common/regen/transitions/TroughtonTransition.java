package me.swirtzly.regen.common.regen.transitions;

import me.swirtzly.regen.client.rendering.transitions.TroughtonTransitionRenderer;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.POVMessage;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

public final class TroughtonTransition implements TransitionType<TroughtonTransitionRenderer> {

    @Override
    public int getAnimationLength() {
        return 330;
    }

    @Override
    public TroughtonTransitionRenderer getRenderer() {
        return TroughtonTransitionRenderer.INSTANCE;
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(PointOfView.THIRD_PERSON_FRONT));
        }
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        LivingEntity living = cap.getLiving();
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(PointOfView.FIRST_PERSON));
        }
        cap.setAnimationTicks(0);
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

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(RConstants.MODID, "troughton");
    }

}
