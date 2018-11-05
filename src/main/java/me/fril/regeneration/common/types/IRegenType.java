package me.fril.regeneration.common.types;

import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public interface IRegenType {
	
	String getName();
	
	default void onStartRegeneration(EntityPlayer player, IRegeneration capability) {}
	default void onUpdateMidRegen(EntityPlayer player, IRegeneration capability) {}
	default void onFinishRegeneration(EntityPlayer player, IRegeneration capability) {}
	
	default void onRenderRegeneratingPlayerPre(RenderPlayerEvent.Pre ev, IRegeneration capability) {}
	default void onRenderRegenerationLayer(RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {}
	
}
