package me.swirtzly.regen.common.regen.transitions;

import me.swirtzly.regen.client.rendering.transitions.LayFadeTransitionRenderer;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.POVMessage;
import me.swirtzly.regen.util.PlayerUtil;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.PacketDistributor;

public class TroughtonTransition implements TransitionType<LayFadeTransitionRenderer> {

    @Override
    public int getAnimationLength() {
        return 500;
    }

    @Override
    public LayFadeTransitionRenderer getRenderer() {
        return LayFadeTransitionRenderer.INSTANCE;
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayerEntity) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) cap.getLiving()), new POVMessage(PointOfView.THIRD_PERSON_FRONT));
        }
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {

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
    public double getAnimationProgress(IRegen cap) {
        return Math.min(1, cap.getTicksAnimating() / (double) getAnimationLength());
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.HAND_GLOW.get()};
    }

    @Override
    public Vector3d getDefaultPrimaryColor() {
        return new Vector3d(1, 1, 1);
    }

    @Override
    public Vector3d getDefaultSecondaryColor() {
        return new Vector3d(1, 1, 1);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(RConstants.MODID, "troughton");
    }
}
