package me.swirtzly.regeneration.common.types;

import me.swirtzly.regeneration.client.rendering.types.ATypeRenderer;
import me.swirtzly.regeneration.common.capability.IRegen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * SUBCLASSES MUST HAVE A DEFAULT CONSTRUCTOR
 * <p>
 * Created by Sub on 16/09/2018.
 */
public interface RegenType<R extends ATypeRenderer<?>> {

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

    double getAnimationProgress(IRegen cap);

    SoundEvent[] getRegeneratingSounds();

    Vec3d getDefaultPrimaryColor();

    Vec3d getDefaultSecondaryColor();

    default TranslationTextComponent getTranslation() {
        return new TranslationTextComponent("type.regeneration." + getRegistryName().getPath());
    }

    ResourceLocation getRegistryName();
}
