package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

public abstract class TransitionType {

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

    public abstract Vec3 getDefaultPrimaryColor();

    public abstract Vec3 getDefaultSecondaryColor();

    public MutableComponent getTranslation() {
        return Component.translatable("type.regeneration." + TransitionTypes.getTransitionId(this).getPath());
    }

    public String getTranslationKey() {
        return "type.regeneration." + TransitionTypes.getTransitionId(this).getPath();
    }

    public void tick(IRegen cap) {
    }

}
