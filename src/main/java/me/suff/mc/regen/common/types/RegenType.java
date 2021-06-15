package me.suff.mc.regen.common.types;

import me.suff.mc.regen.client.rendering.types.ATypeRenderer;
import me.suff.mc.regen.common.capability.IRegen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * SUBCLASSES MUST HAVE A DEFAULT CONSTRUCTOR
 * <p>
 * Created by Craig on 16/09/2018.
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
