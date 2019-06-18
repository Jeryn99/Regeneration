package me.suff.regeneration.client.rendering;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
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
        if (CapabilityRegeneration.getForPlayer(entitylivingbaseIn).areHandsGlowing()) {
            GlStateManager.pushMatrix();

            if (this.livingEntityRenderer.getMainModel().isChild) {
                GlStateManager.translate(0.0F, 0.75F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }

            renderHand(entitylivingbaseIn, EnumHandSide.LEFT, 1.5F);
            renderHand(entitylivingbaseIn, EnumHandSide.RIGHT, 1.5F);

            GlStateManager.popMatrix();
        }
    }

    private void renderHand(EntityPlayer player, EnumHandSide handSide, float scale) {
        GlStateManager.pushMatrix();

        if (player.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }
        // Forge: moved this call down, fixes incorrect offset while sneaking.
        this.translateToHand(handSide);
        boolean flag = handSide == EnumHandSide.LEFT;
        GlStateManager.translate((float) (flag ? -1 : 1) / 25.0F, 0.125F, -0.625F);
        GlStateManager.translate(0, -0.050, 0.6);
        LayerRegeneration.renderGlowingHands(player, CapabilityRegeneration.getForPlayer(player), scale, handSide);

        GlStateManager.popMatrix();
    }

    protected void translateToHand(EnumHandSide p_191361_1_) {
        ((ModelBiped) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, p_191361_1_);
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}