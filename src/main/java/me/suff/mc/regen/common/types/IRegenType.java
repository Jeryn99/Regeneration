package me.suff.mc.regen.common.types;

import me.suff.mc.regen.client.rendering.types.ATypeRenderer;
import me.suff.mc.regen.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

/**
 * SUBCLASSES MUST HAVE A DEFAULT CONSTRUCTOR
 * <p>
 * Created by Sub on 16/09/2018.
 */
public interface IRegenType<R extends ATypeRenderer<?>> {

    /**
     * @return in ticks
     */
    int getAnimationLength();

    R getRenderer();

    default void onStartRegeneration(EntityPlayer player, IRegeneration capability) {
    }

    default void onUpdateMidRegen(EntityPlayer player, IRegeneration capability) {
    }

    default void onFinishRegeneration(EntityPlayer player, IRegeneration capability) {
    }

    double getAnimationProgress(IRegeneration cap);

    TypeHandler.RegenType getTypeID();

    SoundEvent[] getRegeneratingSounds();

    Vec3d getDefaultPrimaryColor();

    Vec3d getDefaultSecondaryColor();

}
