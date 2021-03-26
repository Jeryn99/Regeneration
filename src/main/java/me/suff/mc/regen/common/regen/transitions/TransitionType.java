package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.client.rendering.transitions.TransitionRenderer;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.traits.AbstractTrait;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class TransitionType extends ForgeRegistryEntry< TransitionType> {

    /**
     * @return in ticks
     */
    public abstract int getAnimationLength();

    public void onStartRegeneration(IRegen cap) {
    }

    public void onUpdateMidRegen(IRegen cap) {
    }

    public void onFinishRegeneration(IRegen cap) {
    }

    public double getAnimationProgress(IRegen cap) {
        return Math.min(1, cap.updateTicks() / (double) getAnimationLength());
    }

    public abstract SoundEvent[] getRegeneratingSounds();

    public abstract Vector3d getDefaultPrimaryColor();

    public abstract Vector3d getDefaultSecondaryColor();

    public TranslationTextComponent getTranslation() {
        return new TranslationTextComponent("type.regeneration." + getRegistryName().getPath());
    }

    public void tick(IRegen cap) {
    }

}
