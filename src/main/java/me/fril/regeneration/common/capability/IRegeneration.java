package me.fril.regeneration.common.capability;

import me.fril.regeneration.util.RegenState;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegeneration extends INBTSerializable<NBTTagCompound> {
	
	RegenState getState();
	EntityPlayer getPlayer();
	
	void tick();
	void synchronise();
	
	NBTTagCompound getStyle();
	void setStyle(NBTTagCompound nbt);
	Vec3d getPrimaryColor();
	Vec3d getSecondaryColor();
	
	int getRegenerationsLeft();
	double getAnimationProgress();
	
	/** Returns if the player is currently <i>able to</i> regenerate */
	default boolean canRegenerate() {
		return getRegenerationsLeft() > 0 && getPlayer().posY > 0;
	}
	
	void receiveRegenerations(int amount);
	void extractRegeneration(int amount);
	
	void onRenderRegenerationLayer(RenderPlayer playerRenderer, IRegeneration cap, EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);
	void onRenderRegeneratingPlayerPre(RenderPlayerEvent.Pre event);
	
	IRegenerationStateManager getStateManager();
	
}
