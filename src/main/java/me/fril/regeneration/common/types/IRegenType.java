package me.fril.regeneration.common.types;

import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegenType {
	String getName();
	
	default void onStartRegeneration(EntityPlayer player) {}
	default void onUpdateMidRegen(EntityPlayer player) {}
	default void onFinishRegeneration(EntityPlayer player) {}

	@SideOnly(Side.CLIENT)
	default void onRenderPlayerPre(RenderPlayerEvent.Pre ev) {}

	@SideOnly(Side.CLIENT)
	default void onRenderLayer(RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {}
	
	SoundEvent getSound();
	boolean blockMovement();
	boolean isLaying();
}
