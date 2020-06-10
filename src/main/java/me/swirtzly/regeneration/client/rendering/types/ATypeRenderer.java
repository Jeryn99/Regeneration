package me.swirtzly.regeneration.client.rendering.types;

import me.swirtzly.animateme.AnimationManager;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.types.RegenType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;

public abstract class ATypeRenderer<T> implements AnimationManager.IAnimate {

    protected abstract void onRenderPre(T type, Pre event, IRegen capability);

    protected abstract void onRenderPost(T type, RenderPlayerEvent.Post event, IRegen capability);

    protected abstract void onRenderLayer(T type, LivingRenderer renderLivingBase, IRegen capability, LivingEntity entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);
	
	// Generic casting convenience methods:
	
	@SuppressWarnings("unchecked")
    public final void onRenderPre(RegenType<?> type, Pre event, IRegen capability) {
		try {
            onRenderPre((T) type, event, capability);
		} catch (ClassCastException e) {
			throw new IllegalStateException("RegenType <-> RegenRenderType mismatch", e);
		}
	}
	
	@SuppressWarnings("unchecked")
    public final void onRenderPost(RegenType<?> type, RenderPlayerEvent.Post event, IRegen capability) {
		try {
            onRenderPost((T) type, event, capability);
		} catch (ClassCastException e) {
			throw new IllegalStateException("RegenType <-> RegenRenderType mismatch", e);
		}
	}
	
	@SuppressWarnings("unchecked")
    public final void onRenderLayer(RegenType<?> type, LivingRenderer renderLivingBase, IRegen capability, LivingEntity entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		try {
            onRenderLayer((T) type, renderLivingBase, capability, entityPlayer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		} catch (ClassCastException e) {
			throw new IllegalStateException("RegenType <-> RegenRenderType mismatch", e);
		}
	}

	public abstract void renderHand(LivingEntity player, HandSide handSide, LivingRenderer render);
	
}
