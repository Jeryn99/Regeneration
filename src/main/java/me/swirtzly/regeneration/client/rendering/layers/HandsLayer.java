package me.swirtzly.regeneration.client.rendering.layers;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.types.TypeManager;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;

public class HandsLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {


	private final IEntityRenderer livingEntityRenderer;

	public HandsLayer(IEntityRenderer livingEntityRendererIn) {
		super(livingEntityRendererIn);
		this.livingEntityRenderer = livingEntityRendererIn;
	}
	
	@Override
	public void render(AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();

		CapabilityRegeneration.getForPlayer(entitylivingbaseIn).ifPresent((data) -> {
			if (this.livingEntityRenderer.getEntityModel().isChild) {
				GlStateManager.translatef(0.0F, 0.75F, 0.0F);
				GlStateManager.scalef(0.5F, 0.5F, 0.5F);
			}
			if (data.areHandsGlowing()) {
				renderHand(entitylivingbaseIn, HandSide.LEFT, EnumHandRenderType.GRACE);
				renderHand(entitylivingbaseIn, HandSide.RIGHT, EnumHandRenderType.GRACE);
			}

			if (data.getState() == PlayerUtil.RegenState.REGENERATING || data.isSyncingToJar()) {
				renderHand(entitylivingbaseIn, HandSide.LEFT, EnumHandRenderType.REGEN);
				renderHand(entitylivingbaseIn, HandSide.RIGHT, EnumHandRenderType.REGEN);
			}
		});

		
		GlStateManager.popMatrix();
	}
	
	private void renderHand(PlayerEntity player, HandSide handSide, EnumHandRenderType type) {
		GlStateManager.pushMatrix();

		CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> {
			if (player.isSneaking()) {
				GlStateManager.translatef(0.0F, 0.2F, 0.0F);
			}
			// Forge: moved this call down, fixes incorrect offset while sneaking.
			this.translateToHand(handSide);
			boolean flag = handSide == HandSide.LEFT;
			GlStateManager.translatef((float) (flag ? -1 : 1) / 25.0F, 0.125F, -0.625F);
			GlStateManager.translated(0, -0.050, 0.6);

			if (type == EnumHandRenderType.GRACE) {
				RegenerationLayer.renderGlowingHands(player, data, 1.5F, handSide);
			}

			if (type == EnumHandRenderType.REGEN) {
				TypeManager.getTypeInstance(data.getType()).getRenderer().renderHand(player, handSide, (LivingRenderer) livingEntityRenderer);
			}

		});

		GlStateManager.popMatrix();
	}
	
	protected void translateToHand(HandSide handSide) {
		((BipedModel) this.livingEntityRenderer.getEntityModel()).postRenderArm(0.0625F, handSide);
	}



	public boolean shouldCombineTextures() {
		return false;
	}
	
	public enum EnumHandRenderType {
		REGEN, GRACE
	}
}