package me.swirtzly.regeneration.client.rendering.types;

import me.swirtzly.animateme.AnimationManager;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.RegenType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;

public abstract class ATypeRenderer<T> implements AnimationManager.IAnimate {
	
	protected abstract void renderRegeneratingPlayerPre(T type, Pre event, IRegeneration capability);
	
	protected abstract void renderRegeneratingPlayerPost(T type, RenderPlayerEvent.Post event, IRegeneration capability);
	
	protected abstract void renderRegenerationLayer(T type, LivingRenderer renderLivingBase, IRegeneration capability, PlayerEntity entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);
	
	// Generic casting convenience methods:
	
	@SuppressWarnings("unchecked")
	public final void onRenderRegeneratingPlayerPre(RegenType<?> type, Pre event, IRegeneration capability) {
		try {
			renderRegeneratingPlayerPre((T) type, event, capability);
		} catch (ClassCastException e) {
			throw new IllegalStateException("RegenType <-> RegenRenderType mismatch", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public final void onRenderRegeneratingPlayerPost(RegenType<?> type, RenderPlayerEvent.Post event, IRegeneration capability) {
		try {
			renderRegeneratingPlayerPost((T) type, event, capability);
		} catch (ClassCastException e) {
			throw new IllegalStateException("RegenType <-> RegenRenderType mismatch", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public final void onRenderRegenerationLayer(RegenType<?> type, LivingRenderer renderLivingBase, IRegeneration capability, PlayerEntity entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		try {
			renderRegenerationLayer((T) type, renderLivingBase, capability, entityPlayer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		} catch (ClassCastException e) {
			throw new IllegalStateException("RegenType <-> RegenRenderType mismatch", e);
		}
	}

	
	public abstract void renderHand(PlayerEntity player, HandSide handSide, LivingRenderer render);
	
}
