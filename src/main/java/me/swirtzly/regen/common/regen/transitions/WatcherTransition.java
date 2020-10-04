package me.swirtzly.regen.common.regen.transitions;

import me.swirtzly.regen.client.rendering.transitions.WatcherTransitionRenderer;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;

public class WatcherTransition implements TransitionType<WatcherTransitionRenderer> {

    @Override
    public int getAnimationLength() {
        return 200;
    }

    @Override
    public WatcherTransitionRenderer getRenderer() {
        return WatcherTransitionRenderer.INSTANCE;
    }

    @Override
    public double getAnimationProgress(IRegen cap) {
        return Math.min(1, cap.getTicksAnimating() / (double) getAnimationLength());
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.REGENERATION_TROUGHTON.get()};
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
    public void onFinishRegeneration(IRegen cap) {
        LivingEntity living = cap.getLiving();
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        LivingEntity living = cap.getLiving();
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(RConstants.MODID, "watcher");
    }
}
