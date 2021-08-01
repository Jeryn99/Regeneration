package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class TransitionType extends ForgeRegistryEntry<TransitionType> {

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

    public TranslatableComponent getTranslation() {
        return new TranslatableComponent("type.regeneration." + getRegistryName().getPath());
    }

    public void tick(IRegen cap) {
    }

}
