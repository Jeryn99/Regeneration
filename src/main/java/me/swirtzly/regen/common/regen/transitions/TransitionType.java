package me.swirtzly.regen.common.regen.transitions;

import me.swirtzly.regen.client.rendering.transitions.TransitionRenderer;
import me.swirtzly.regen.common.regen.IRegen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * SUBCLASSES MUST HAVE A DEFAULT CONSTRUCTOR
 * <p>
 * Created by Sub on 16/09/2018.
 */
public interface TransitionType<R extends TransitionRenderer> {

    /**
     * @return in ticks
     */
    int getAnimationLength();

    R getRenderer();

    default void onStartRegeneration(IRegen cap) {
    }

    default void onUpdateMidRegen(IRegen cap) {
    }

    default void onFinishRegeneration(IRegen cap) {
    }

    default double getAnimationProgress(IRegen cap) {
        return Math.min(1, cap.getTicksAnimating() / (double) getAnimationLength());
    }

    SoundEvent[] getRegeneratingSounds();

    Vector3d getDefaultPrimaryColor();

    Vector3d getDefaultSecondaryColor();

    default TranslationTextComponent getTranslation() {
        return new TranslationTextComponent("type.regeneration." + getRegistryName().getPath());
    }

    ResourceLocation getRegistryName();

    default void tick(IRegen cap) {
    }

    default boolean isPlayerOnly(){
        return false;
    }

}
