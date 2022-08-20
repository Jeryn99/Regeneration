package craig.software.mc.regen.client.rendering.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.regen.transitions.TransitionType;
import craig.software.mc.regen.common.regen.transitions.TransitionTypeRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RenderRegenLayer extends RenderLayer {

    public RenderRegenLayer(RenderLayerParent entityRendererIn) {
        super(entityRendererIn);
    }

    public static void renderColorCone(PoseStack poseStack, VertexConsumer vertexBuilder, int combinedLightIn, Entity entityPlayer, float scale, float scale2, Vec3 color) {
        RegenCap.get((LivingEntity) entityPlayer).ifPresent(iRegen -> {
            poseStack.pushPose();
            for (int i = 0; i < 10; i++) {
                poseStack.mulPose(Vector3f.YP.rotation(entityPlayer.tickCount * 4 + i * 45));
                poseStack.scale(1.0f, 1.0f, 0.65f);
                float alpha = Mth.clamp(Mth.sin((entityPlayer.tickCount + Minecraft.getInstance().getFrameTime()) / 5) * 0.1F + 0.1F, 0.11F, 1F);
                float red = (float) color.x, green = (float) color.y, blue = (float) color.z;
                vertexBuilder.vertex(poseStack.last().pose(), 0.0F, 0.0F, 0.0F).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
                vertexBuilder.vertex(poseStack.last().pose(), -0.266F * scale, scale, -0.5F * scale).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
                vertexBuilder.vertex(poseStack.last().pose(), 0.266F * scale, scale, -0.5F * scale).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
                vertexBuilder.vertex(poseStack.last().pose(), 0.0F, scale2, 1.0F * scale).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
                vertexBuilder.vertex(poseStack.last().pose(), (float) (-0.266D * scale), scale, -0.5F * scale).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
            }
            poseStack.popPose();
        });
    }


    @Override
    public void render(@NotNull PoseStack poseStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn instanceof LivingEntity livingEntity) {
            RegenCap.get(livingEntity).ifPresent(iRegen -> {
                TransitionType type = iRegen.transitionType();
                TransitionTypeRenderers.get(type).layer((HumanoidModel<?>) getParentModel(), poseStackIn, bufferIn, packedLightIn, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            });
        }
    }
}
