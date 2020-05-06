package me.swirtzly.regeneration.common.types;

import me.swirtzly.regeneration.client.rendering.types.ATypeRenderer;
import me.swirtzly.regeneration.common.capability.IRegen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

/**
 * SUBCLASSES MUST HAVE A DEFAULT CONSTRUCTOR
 * <p>
 * Created by Sub on 16/09/2018.
 */
public interface RegenType<R extends ATypeRenderer<?>> {

    static RegenType<?> getType(TypeManager.Type type) {

        return TypeManager.getTypeInstance(TypeManager.Type.FIERY);
	}
	
	/**
	 * @return in ticks
	 */
	int getAnimationLength();
	
	R getRenderer();

    default void onStartRegeneration(LivingEntity player, IRegen capability) {
    }

    default void onUpdateMidRegen(LivingEntity player, IRegen capability) {
    }

    default void onFinishRegeneration(LivingEntity player, IRegen capability) {
    }

    double getAnimationProgress(IRegen cap);
	
	TypeManager.Type getTypeID();

    SoundEvent[] getRegeneratingSounds();

    Vec3d getDefaultPrimaryColor();

    Vec3d getDefaultSecondaryColor();
}
