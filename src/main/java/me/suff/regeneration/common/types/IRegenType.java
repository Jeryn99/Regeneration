package me.suff.regeneration.common.types;

import me.suff.regeneration.client.rendering.ATypeRenderer;
import me.suff.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * SUBCLASSES MUST HAVE A DEFAULT CONSTRUCTOR
 * <p>
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegenType<R extends ATypeRenderer<?>> extends INBTSerializable<NBTTagCompound> {
	
	static IRegenType<?> getType(IRegenType<?> currentType, NBTTagCompound nbt) {
		try {
			Class<?> nbtClass = Class.forName(nbt.getString("name"));
			
			if (currentType == null || currentType.getClass() != nbtClass) { // no current type OR type has changed
				return (IRegenType<?>) nbtClass.newInstance();
			} else {
				currentType.deserializeNBT(nbt);
				return currentType;
			}
		} catch (ReflectiveOperationException | ClassCastException e) {
			System.err.println("WARNING: Malformed type NBT, reverting to default");
			e.printStackTrace();
			return new TypeFiery();
		}
	}
	
	/**
	 * @return in ticks
	 */
	int getAnimationLength();
	
	R getRenderer();
	
	default void onStartRegeneration(EntityPlayer player, LazyOptional<IRegeneration> capability) {
	}
	
	default void onUpdateMidRegen(EntityPlayer player, LazyOptional<IRegeneration> capability) {
	}
	
	default void onFinishRegeneration(EntityPlayer player, LazyOptional<IRegeneration> capability) {
	}
	
	double getAnimationProgress();
	
	@Override
	default NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", this.getClass().getName());
		return nbt;
	}
	
	@Override
	default void deserializeNBT(NBTTagCompound nbt) {
		if (!nbt.getString("name").equals(this.getClass().getName()))
			throw new IllegalStateException("Deserialising wrong type instance (nbt: " + nbt.getString("name") + ", instance: " + this.getClass().getName());
	}
	
}
