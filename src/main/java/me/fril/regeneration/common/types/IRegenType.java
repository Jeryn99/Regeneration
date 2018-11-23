package me.fril.regeneration.common.types;

import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * SUBCLASSES MUST HAVE A DEFAULT CONSTRUCTOR
 * 
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegenType extends INBTSerializable<NBTTagCompound> {
	
	/** @return in ticks */
	int getAnimationLength();
	
	default void onStartRegeneration(EntityPlayer player, IRegeneration capability) {}
	default void onUpdateMidRegen(EntityPlayer player, IRegeneration capability) {}
	default void onFinishRegeneration(EntityPlayer player, IRegeneration capability) {}
	
	@SideOnly(Side.CLIENT)
	default void onRenderRegeneratingPlayerPre(RenderPlayerEvent.Pre ev, IRegeneration capability) {}
	@SideOnly(Side.CLIENT)
	default void onRenderRegenerationLayer(RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {}
	
	
	
	@Override
	default NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", this.getClass().getName());
		return nbt;
	}
	
	@Override
	default void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.getString("name") != this.getClass().getName())
			throw new IllegalStateException("Deserialising wrong type instance (nbt: "+nbt.getString("name")+", instance: "+this.getClass().getName());
	}
	
	
	
	public static IRegenType getType(NBTTagCompound nbt) {
		try {
			return (IRegenType) Class.forName(nbt.getString("name")).newInstance();
		} catch (ReflectiveOperationException e) {
			System.err.println("WARNING: Malformed type NBT, reverting to default");
			e.printStackTrace();
			return new TypeFiery();
		}
	}
	
}
