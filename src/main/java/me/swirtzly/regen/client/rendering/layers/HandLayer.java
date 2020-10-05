package me.swirtzly.regen.client.rendering.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.client.rendering.types.RenderTypes;
import me.swirtzly.regen.common.regen.RegenCap;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3d;

import static me.swirtzly.regen.client.rendering.layers.RenderRegenLayer.renderCone;

public class HandLayer extends LayerRenderer {


    public HandLayer(IEntityRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    public static void renderGlowingHands(LivingEntity livingEntity, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            if (iRegen.areHandsGlowing()) {
                Vector3d primaryColors = iRegen.getPrimaryColors();
                Vector3d secondaryColors = iRegen.getSecondaryColors();
                renderCone(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, livingEntity, 0.5F, 0.5F, primaryColors);
                renderCone(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, livingEntity, 0.7F, 0.7F, secondaryColors);
            }
        });
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityModel<?> model = getEntityModel();
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {

            if (entitylivingbaseIn.isSneaking()) {
                matrixStackIn.translate(0.0F, 0.2F, 0.0F);
            }

            BipedModel<?> bipedModel = (BipedModel) model;

            //For Regen Layers
            for (HandSide handSide : HandSide.values()) {
                matrixStackIn.push();
                bipedModel.translateHand(handSide, matrixStackIn);
                renderGlowingHands((LivingEntity) entitylivingbaseIn, matrixStackIn, bufferIn, packedLightIn);
                iRegen.getTransitionType().get().getRenderer().thirdPersonHand(handSide, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                matrixStackIn.pop();
            }
        });
    }
}
