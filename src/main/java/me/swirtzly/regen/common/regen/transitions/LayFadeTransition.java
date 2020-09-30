package me.swirtzly.regen.common.regen.transitions;

import me.swirtzly.regen.client.rendering.transitions.LayFadeTransitionRenderer;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;

public class LayFadeTransition implements TransitionType<LayFadeTransitionRenderer> {


    @Override
    public int getAnimationLength() {
        return 0;
    }

    @Override
    public LayFadeTransitionRenderer getRenderer() {
        return LayFadeTransitionRenderer.INSTANCE;
    }

    @Override
    public void onStartRegeneration(IRegen cap) {

    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {

    }

    @Override
    public void onFinishRegeneration(IRegen cap) {

    }

    @Override
    public double getAnimationProgress(IRegen cap) {
        return 0;
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[0];
    }

    @Override
    public Vector3d getDefaultPrimaryColor() {
        return null;
    }

    @Override
    public Vector3d getDefaultSecondaryColor() {
        return null;
    }

    @Override
    public TranslationTextComponent getTranslation() {
        return null;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(RConstants.MODID, "lay_fade");
    }
}
