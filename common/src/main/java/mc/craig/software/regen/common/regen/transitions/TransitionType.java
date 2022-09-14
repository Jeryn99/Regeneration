package mc.craig.software.regen.common.regen.transitions;

import mc.craig.software.regen.common.regen.IRegen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

public abstract class TransitionType {

    private ResourceLocation location;

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

    public ResourceLocation getLocation() {
        return location;
    }

    public TransitionType setLocation(ResourceLocation resourceLocation) {
        this.location = resourceLocation;
        return this;
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

    @Override
    public String toString() {
        return "TransitionType{" +
                "location=" + location +
                '}';
    }

    public void tick(IRegen cap) {
    }

}
