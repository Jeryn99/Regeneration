package me.swirtzly.regeneration.client.rendering.layers;

import me.swirtzly.regeneration.client.rendering.types.TypeFieryRenderer;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.TypeHandler;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;

public class LayerHands implements LayerRenderer<EntityPlayer> {
    protected final RenderLivingBase<?> livingEntityRenderer;

    public LayerHands(RenderLivingBase<?> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }


    public void doRenderLayer(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();

        IRegeneration data = CapabilityRegeneration.getForPlayer(entitylivingbaseIn);

        if (this.livingEntityRenderer.getMainModel().isChild) {
            GlStateManager.translate(0.0F, 0.75F, 0.0F);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
        }
        if (data.areHandsGlowing()) {
            renderHand(entitylivingbaseIn, EnumHandSide.LEFT, EnumHandRenderType.GRACE);
            renderHand(entitylivingbaseIn, EnumHandSide.RIGHT, EnumHandRenderType.GRACE);
        }

        if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
            renderHand(entitylivingbaseIn, EnumHandSide.LEFT, EnumHandRenderType.REGEN);
            renderHand(entitylivingbaseIn, EnumHandSide.RIGHT, EnumHandRenderType.REGEN);
        }

        if (data.isSyncingToJar()) {
            renderHand(entitylivingbaseIn, EnumHandSide.LEFT, EnumHandRenderType.JAR);
            renderHand(entitylivingbaseIn, EnumHandSide.RIGHT, EnumHandRenderType.JAR);
        }

        GlStateManager.popMatrix();
    }

    public void renderHand(EntityPlayer player, EnumHandSide handSide, EnumHandRenderType type) {
        GlStateManager.pushMatrix();

        IRegeneration data = CapabilityRegeneration.getForPlayer(player);

        if (player.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }

        this.translateToHand(handSide);
        boolean flag = handSide == EnumHandSide.LEFT;
        GlStateManager.translate((float) (flag ? -1 : 1) / 25.0F, 0.125F, -0.625F);
        GlStateManager.translate(0, -0.050, 0.6);

        if (type == EnumHandRenderType.GRACE) {
            LayerRegeneration.renderGlowingHands(player, data, 1.5F, handSide);
        }

        if (type == EnumHandRenderType.REGEN) {
            TypeHandler.getTypeInstance(data.getType()).getRenderer().renderHand(player, handSide, livingEntityRenderer);
        }

        if (type == EnumHandRenderType.JAR) {
            TypeFieryRenderer.renderConeAtArms(player);
        }

        GlStateManager.popMatrix();
    }

    protected void translateToHand(EnumHandSide handSide) {
        ((ModelBiped) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, handSide);
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    public enum EnumHandRenderType {
        REGEN, GRACE, JAR
    }
}