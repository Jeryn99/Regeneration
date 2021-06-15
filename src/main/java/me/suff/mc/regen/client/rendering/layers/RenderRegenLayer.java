package me.suff.mc.regen.client.rendering.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.common.regen.transitions.TransitionTypeRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RenderRegenLayer extends LayerRenderer {

    public RenderRegenLayer(IEntityRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    public static void renderColorCone(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int combinedLightIn, LivingEntity entityPlayer, float scale, float scale2, Vector3d color) {
        RegenCap.get(entityPlayer).ifPresent(iRegen -> {
            matrixStack.pushPose();
            for (int i = 0; i < 10; i++) {
                matrixStack.mulPose(Vector3f.YP.rotation(entityPlayer.tickCount * 4 + i * 45));
                matrixStack.scale(1.0f, 1.0f, 0.65f);
                float alpha = MathHelper.clamp(MathHelper.sin((entityPlayer.tickCount + Minecraft.getInstance().getFrameTime()) / 5) * 0.1F + 0.1F, 0.11F, 1F);
                float red = (float) color.x, green = (float) color.y, blue = (float) color.z;
                vertexBuilder.vertex(matrixStack.last().pose(), 0.0F, 0.0F, 0.0F).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
                vertexBuilder.vertex(matrixStack.last().pose(), -0.266F * scale, scale, -0.5F * scale).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
                vertexBuilder.vertex(matrixStack.last().pose(), 0.266F * scale, scale, -0.5F * scale).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
                vertexBuilder.vertex(matrixStack.last().pose(), 0.0F, scale2, 1.0F * scale).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
                vertexBuilder.vertex(matrixStack.last().pose(), (float) (-0.266D * scale), scale, -0.5F * scale).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
            }
            matrixStack.popPose();
        });
    }


    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn instanceof LivingEntity) {
            RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
                TransitionType type = iRegen.transitionType();
                TransitionTypeRenderers.get(type).layer((BipedModel<?>) getParentModel(), matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            });
        }
    }
}
