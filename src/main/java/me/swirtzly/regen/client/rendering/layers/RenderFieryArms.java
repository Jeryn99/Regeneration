package me.swirtzly.regen.client.rendering.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.client.rendering.types.RenderTypes;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RenderFieryArms extends LayerRenderer {

    public RenderFieryArms(IEntityRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    //TODO Move to the TransitionRenderer
    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityModel model = getEntityModel();
        if (model instanceof BipedModel) {
            BipedModel bipedModel = (BipedModel) model;
            RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
                CompoundNBT colorTag = iRegen.getOrWriteStyle();
                Vector3d primaryColors = new Vector3d(colorTag.getFloat(RConstants.PRIMARY_RED), colorTag.getFloat(RConstants.PRIMARY_GREEN), colorTag.getFloat(RConstants.PRIMARY_BLUE));
                Vector3d secondaryColors = new Vector3d(colorTag.getFloat(RConstants.SECONDARY_RED), colorTag.getFloat(RConstants.SECONDARY_GREEN), colorTag.getFloat(RConstants.SECONDARY_BLUE));

                for (HandSide handSide : HandSide.values()) {
                    matrixStackIn.push();
                    bipedModel.translateHand(handSide, matrixStackIn);
                    renderCone(matrixStackIn, bufferIn, packedLightIn, (LivingEntity) entitylivingbaseIn, 1F, 1F, primaryColors);
                    renderCone(matrixStackIn, bufferIn, packedLightIn, (LivingEntity) entitylivingbaseIn, 1.5F, 1.5F, secondaryColors);
                    matrixStackIn.pop();
                }
            });
        }
    }


    public static void renderCone(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, LivingEntity entityPlayer, float scale, float scale2, Vector3d color) {
        matrixStack.push();
        RegenCap.get(entityPlayer).ifPresent(iRegen -> {
            IVertexBuilder vertexBuilder = bufferIn.getBuffer(RenderTypes.LASER);
            for (int i = 0; i < 8; i++) {
                matrixStack.rotate(Vector3f.YP.rotation(entityPlayer.ticksExisted * 4 + i * 45));
                matrixStack.scale(1.0f, 1.0f, 0.65f);
                float red = (float) color.x, green = (float) color.y, blue = (float) color.z, alpha = 55;
                vertexBuilder.pos(matrixStack.getLast().getMatrix(), 0.0F, 0.0F, 0.0F).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
                vertexBuilder.pos(matrixStack.getLast().getMatrix(), -0.266F * scale, scale, -0.5F * scale).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
                vertexBuilder.pos(matrixStack.getLast().getMatrix(), 0.266F * scale, scale, -0.5F * scale).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
                vertexBuilder.pos(matrixStack.getLast().getMatrix(), 0.0F, scale2, 1.0F * scale).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
                vertexBuilder.pos(matrixStack.getLast().getMatrix(), (float) (-0.266D * scale), scale, -0.5F * scale).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
            }
            matrixStack.pop();
        });
    }
}
