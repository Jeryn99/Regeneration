package me.suff.mc.regen.client.rendering.layers;

import com.mojang.blaze3d.platform.GlStateManager;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;

public class HandsLayer extends LayerRenderer {

    private final IEntityRenderer livingEntityRenderer;

    public HandsLayer(IEntityRenderer livingEntityRendererIn) {
        super(livingEntityRendererIn);
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        LivingEntity entitylivingbaseIn = (LivingEntity) entity;
        RegenCap.get(entitylivingbaseIn).ifPresent((data) -> {
            if (this.livingEntityRenderer.getModel().young) {
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

    private void renderHand(LivingEntity player, HandSide handSide, EnumHandRenderType type) {
        GlStateManager.pushMatrix();

        RegenCap.get(player).ifPresent((data) -> {
            if (player.isSneaking()) {
                GlStateManager.translatef(0.0F, 0.2F, 0.0F);
            }

            this.translateToHand(handSide);
            boolean flag = handSide == HandSide.LEFT;
            GlStateManager.translatef((float) (flag ? -1 : 1) / 25.0F, 0.125F, -0.625F);
            GlStateManager.translated(0, -0.050, 0.6);

            if (type == EnumHandRenderType.GRACE) {
                RegenerationLayer.renderGlowingHands(player, data, 1.5F, handSide);
            }

            if (type == EnumHandRenderType.REGEN) {
                data.getRegenType().create().getRenderer().renderHand(player, handSide, (LivingRenderer) livingEntityRenderer);
            }

        });

        GlStateManager.popMatrix();
    }

    protected void translateToHand(HandSide handSide) {
        BipedModel biped = ((BipedModel) this.livingEntityRenderer.getModel());
        boolean oldValueRight = biped.rightArm.neverRender;
        boolean oldValueLeft = biped.leftArm.neverRender;
        biped.rightArm.neverRender = false;
        biped.leftArm.neverRender = false;
        biped.translateToHand(0.0625F, handSide);
        biped.rightArm.neverRender = oldValueRight;
        biped.leftArm.neverRender = oldValueLeft;
    }

    public boolean colorsOnDamage() {
        return false;
    }

    public enum EnumHandRenderType {
        REGEN, GRACE
    }
}
