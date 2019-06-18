package me.swirtzly.regeneration.common.types;

import me.swirtzly.regeneration.client.rendering.ATypeRenderer;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;

/**
 * SUBCLASSES MUST HAVE A DEFAULT CONSTRUCTOR
 * <p>
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegenType<R extends ATypeRenderer<?>> {

	static IRegenType<?> getType(TypeHandler.RegenType type) {

		return TypeHandler.getTypeInstance(TypeHandler.RegenType.FIERY);
	}
	
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

	
}
