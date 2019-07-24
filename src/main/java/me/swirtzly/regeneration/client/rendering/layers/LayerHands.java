package me.swirtzly.regeneration.client.rendering.layers;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.TypeHandler;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.model.ModelBiped;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;

public class LayerHands implements LayerRenderer<PlayerEntity> {

	protected final LivingRenderer<?> livingEntityRenderer;
	
	public LayerHands(LivingRenderer<?> livingEntityRendererIn) {
		this.livingEntityRenderer = livingEntityRendererIn;
	}
	
	@Override
	public void render(PlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		
		IRegeneration data = CapabilityRegeneration.getForPlayer(entitylivingbaseIn);
		
		if (this.livingEntityRenderer.getModel().isChild) {
			GlStateManager.translate(0.0F, 0.75F, 0.0F);
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
		}
		if (data.areHandsGlowing()) {
			renderHand(entitylivingbaseIn, HandSide.LEFT, EnumHandRenderType.GRACE);
			renderHand(entitylivingbaseIn, HandSide.RIGHT, EnumHandRenderType.GRACE);
		}
		
		if (data.getState() == PlayerUtil.RegenState.REGENERATING || data.isSyncingToJar()) {
			renderHand(entitylivingbaseIn, HandSide.LEFT, EnumHandRenderType.REGEN);
			renderHand(entitylivingbaseIn, HandSide.RIGHT, EnumHandRenderType.REGEN);
		}
		
		GlStateManager.popMatrix();
	}
	
	private void renderHand(PlayerEntity player, HandSide handSide, EnumHandRenderType type) {
		GlStateManager.pushMatrix();
		
		IRegeneration data = CapabilityRegeneration.getForPlayer(player);
		
		if (player.isSneaking()) {
			GlStateManager.translatef(0.0F, 0.2F, 0.0F);
		}
		// Forge: moved this call down, fixes incorrect offset while sneaking.
		this.translateToHand(handSide);
		boolean flag = handSide == HandSide.LEFT;
		GlStateManager.translatef((float) (flag ? -1 : 1) / 25.0F, 0.125F, -0.625F);
		GlStateManager.translated(0, -0.050, 0.6);
		
		if (type == EnumHandRenderType.GRACE) {
			LayerRegeneration.renderGlowingHands(player, data, 1.5F, handSide);
		}
		
		if (type == EnumHandRenderType.REGEN) {
			TypeHandler.getTypeInstance(data.getType()).getRenderer().renderHand(player, handSide, livingEntityRenderer);
		}
		GlStateManager.popMatrix();
	}
	
	protected void translateToHand(HandSide handSide) {
		((ModelBiped) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, handSide);
	}


	public boolean shouldCombineTextures() {
		return false;
	}
	
	public enum EnumHandRenderType {
		REGEN, GRACE
	}
}