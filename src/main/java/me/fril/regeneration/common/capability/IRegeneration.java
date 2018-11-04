package me.fril.regeneration.common.capability;

import java.awt.Color;

import me.fril.regeneration.util.RegenState;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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
	Color getPrimaryColor();
	Color getSecondaryColor();
	
	int getRegenerationsLeft();
	
	/** Returns if the player is currently <i>able to</i> regenerate */
	default boolean canRegenerate() {
		return getRegenerationsLeft() > 0 && getPlayer().posY > 0;
	}
	
	void receiveRegenerations(int amount);
	void extractRegeneration(int amount);
	
	void onRenderRegenerationLayer(RenderPlayer playerRenderer, IRegeneration cap, EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);
	void onRenderRegeneratingPlayerPre(RenderPlayerEvent.Pre event);
	
	/*
	// Update
	void update();
	
	// Regen Ticks
	int getTicksRegenerating();
	void setTicksRegenerating(int ticks);
	
	// Returns the player
	EntityPlayer getPlayer();
	
	// Lives left
	int getLivesLeft();
	void setLivesLeft(int left);
	
	// Times IN TOTAL, NOT PER CYCLE
	int getTimesRegenerated();
	void setTimesRegenerated(int times);
	
	// Style
	NBTTagCompound getStyle();
	void setStyle(NBTTagCompound nbt);
	
	// Sync to clients
	void sync();
	
	// The type of Regeneration in use
	IRegenType getType();
	void setType(String name);
	
	// Does the player have the ability to regenerate?
	default boolean isCapable() {
		return getLivesLeft() > 0 && getPlayer().posY > 0;
	}
	
	// Regen
	boolean isRegenerating();
	void setRegenerating(boolean regenerating);
	
	// Grace stuff
	boolean isGlowing();
	void setGlowing(boolean glowing);
	
	// Glowing Ticks
	int getTicksGlowing();
	void setTicksGlowing(int ticks);
	
	// Is the player in grace period
	boolean isInGracePeriod();
	void setInGracePeriod(boolean gracePeriod);
	
	// Solace ticks
	int getSolaceTicks();
	void setSolaceTicks(int ticks);
	
	// Just helper things
	Color getPrimaryColor();
	Color getSecondaryColor();
	void reset();
	*/
	
}
