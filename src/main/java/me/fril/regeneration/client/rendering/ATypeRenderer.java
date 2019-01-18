package me.fril.regeneration.client.rendering;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.types.IRegenType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;

public abstract class ATypeRenderer<T> {
	
	protected abstract void renderRegeneratingPlayerPre(T type, Pre event, IRegeneration capability);
	
	protected abstract void renderRegeneratingPlayerPost(T type, Pre event, IRegeneration capability);
	
	protected abstract void renderRegenerationLayer(T type, RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);
	
	// Generic casting convenience methods:
	
	@SuppressWarnings("unchecked")
	public final void onRenderRegeneratingPlayerPre(IRegenType<?> type, Pre event, IRegeneration capability) {
		try {
			renderRegeneratingPlayerPre((T) type, event, capability);
		} catch (ClassCastException e) {
			throw new IllegalStateException("RegenType <-> RegenRenderType mismatch", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public final void onRenderRegenerationLayer(IRegenType<?> type, RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		try {
			renderRegenerationLayer((T) type, renderLivingBase, capability, entityPlayer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		} catch (ClassCastException e) {
			throw new IllegalStateException("RegenType <-> RegenRenderType mismatch", e);
		}
	}
	
}
