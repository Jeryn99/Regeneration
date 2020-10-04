package me.swirtzly.regen.client.rendering.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.common.regen.RegenCap;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;

public class HandLayer extends LayerRenderer {


    public HandLayer(IEntityRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityModel<?> model = getEntityModel();

        if (entitylivingbaseIn.isSneaking()) {
            matrixStackIn.translate(0.0F, 0.2F, 0.0F);
        }

        BipedModel<?> bipedModel = (BipedModel) model;
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
            for (HandSide handSide : HandSide.values()) {
                matrixStackIn.push();
                bipedModel.translateHand(handSide, matrixStackIn);
                iRegen.getTransitionType().create().getRenderer().thirdPersonHand(handSide, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                matrixStackIn.pop();
            }
        });
    }
}
