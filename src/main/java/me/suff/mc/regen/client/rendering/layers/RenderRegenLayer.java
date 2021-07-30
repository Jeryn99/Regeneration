package me.suff.mc.regen.client.rendering.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.common.regen.transitions.TransitionTypeRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;

public class RenderRegenLayer extends RenderLayer {

    public RenderRegenLayer(RenderLayerParent entityRendererIn) {
        super(entityRendererIn);
    }

    public static void renderColorCone(PoseStack matrixStack, VertexConsumer vertexBuilder, int combinedLightIn, LivingEntity entityPlayer, float scale, float scale2, Vec3 color) {
        RegenCap.get(entityPlayer).ifPresent(iRegen -> {
            matrixStack.pushPose();
            for (int i = 0; i < 10; i++) {
                matrixStack.mulPose(Vector3f.YP.rotation(entityPlayer.tickCount * 4 + i * 45));
                matrixStack.scale(1.0f, 1.0f, 0.65f);
                float alpha = Mth.clamp(Mth.sin((entityPlayer.tickCount + Minecraft.getInstance().getFrameTime()) / 5) * 0.1F + 0.1F, 0.11F, 1F);
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
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn instanceof LivingEntity) {
            RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
                TransitionType type = iRegen.transitionType();
                TransitionTypeRenderers.get(type).layer((HumanoidModel<?>) getParentModel(), matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            });
        }
    }
}
