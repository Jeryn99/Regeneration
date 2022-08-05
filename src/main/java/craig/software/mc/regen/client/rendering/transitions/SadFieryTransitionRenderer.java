package craig.software.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import craig.software.mc.regen.client.animation.AnimationHandler;
import craig.software.mc.regen.client.rendering.layers.HandLayer;
import craig.software.mc.regen.client.rendering.types.RenderTypes;
import craig.software.mc.regen.common.entities.Timelord;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.regen.state.RegenStates;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

import static craig.software.mc.regen.client.rendering.layers.RenderRegenLayer.renderColorCone;

public class SadFieryTransitionRenderer implements TransitionRenderer {

    public static final SadFieryTransitionRenderer INSTANCE = new SadFieryTransitionRenderer();

    public static void renderOverlay(PoseStack matrixStack, VertexConsumer buffer, int packedlight, HumanoidModel bipedModel, LivingEntity entityPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float opacity, Vec3 color) {
        renderOverlay(matrixStack, buffer, packedlight, bipedModel, entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, color, null);
    }

    public static void renderOverlay(PoseStack matrixStack, VertexConsumer buffer, int packedlight, HumanoidModel bipedModel, LivingEntity entityPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float opacity, Vec3 color, AnimationHandler.Animation animation) {
        RegenCap.get(entityPlayer).ifPresent((data) -> {
            matrixStack.pushPose();
            if (animation == null) {
                bipedModel.setupAnim(entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            } else {
                animation.animate(bipedModel, entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            }
            bipedModel.renderToBuffer(matrixStack, buffer, packedlight, OverlayTexture.NO_OVERLAY, (float) color.x, (float) color.y, (float) color.z, opacity);
            matrixStack.popPose();
        });
    }

    @Override
    public void onPlayerRenderPre(RenderPlayerEvent.Pre pre) {

    }

    @Override
    public void onPlayerRenderPost(RenderPlayerEvent.Post post) {

    }

    @Override
    public void firstPersonHand(RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(HumanoidArm side, PoseStack matrix, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn != null) {
            RegenCap.get(entitylivingbaseIn).ifPresent(iRegen -> {
                if (iRegen.regenState() == RegenStates.REGENERATING) {

                    double x = iRegen.updateTicks();
                    if (iRegen.updateTicks() > 300 && iRegen.updateTicks() < 920) {
                        double p = 109.89010989010987; // see the wiki for the explanation of these "magic" numbers
                        double r = 0.09890109890109888;
                        double f = p * Math.pow(x, 2) - r;

                        float cf = Mth.clamp((float) f, 0F, 1F);
                        float primaryScale = cf * 4F;
                        float secondaryScale = cf * 6.4F;

                        Vec3 primaryColors = iRegen.getPrimaryColors();
                        Vec3 secondaryColors = iRegen.getSecondaryColors();
                        renderColorCone(matrix, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, entitylivingbaseIn, primaryScale, primaryScale, primaryColors);
                        renderColorCone(matrix, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, entitylivingbaseIn, secondaryScale, secondaryScale, secondaryColors);
                    } else {
                        matrix.pushPose();
                        HandLayer.renderGlowingHands(entitylivingbaseIn, matrix, bufferIn, packedLightIn, side);
                        matrix.popPose();
                    }
                }
            });
        }

    }

    @Override
    public void layer(HumanoidModel<?> bipedModel, PoseStack matrix, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING && iRegen.updateTicks() > 300 && iRegen.updateTicks() < 920) {
                // === Head Cone ===
                matrix.pushPose();
                bipedModel.head.translateAndRotate(matrix);
                matrix.translate(0.0f, 0.09f, 0.2f);
                matrix.mulPose(Vector3f.XP.rotation(180));
                double x = iRegen.updateTicks();
                double p = 109.89010989010987; // see the wiki for the explanation of these "magic" numbers
                double r = 0.09890109890109888;
                double f = p * Math.pow(x, 2) - r;

                float cf = Mth.clamp((float) f, 0F, 1F);
                float primaryScale = cf * 4F;
                float secondaryScale = cf * 6.4F;

                CompoundTag colorTag = iRegen.getOrWriteStyle();
                Vec3 primaryColors = new Vec3(colorTag.getFloat(RConstants.PRIMARY_RED), colorTag.getFloat(RConstants.PRIMARY_GREEN), colorTag.getFloat(RConstants.PRIMARY_BLUE));
                Vec3 secondaryColors = new Vec3(colorTag.getFloat(RConstants.SECONDARY_RED), colorTag.getFloat(RConstants.SECONDARY_GREEN), colorTag.getFloat(RConstants.SECONDARY_BLUE));
                renderColorCone(matrix, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, iRegen.getLiving(), primaryScale, primaryScale, primaryColors);
                renderColorCone(matrix, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, iRegen.getLiving(), secondaryScale, secondaryScale, secondaryColors);
                matrix.popPose();
                // === End Head Cone ===
            }

            //Render player overlay
            if (entitylivingbaseIn.hurtTime > 0 && iRegen.regenState() == RegenStates.POST || iRegen.regenState() == RegenStates.REGENERATING && iRegen.updateTicks() > 300 && iRegen.updateTicks() < 920) {
                if (entitylivingbaseIn instanceof Timelord) return;
                float opacity = Mth.clamp(Mth.sin((entitylivingbaseIn.tickCount + Minecraft.getInstance().getFrameTime()) / 5) * 0.1F + 0.1F, 0.11F, 1F);
                renderOverlay(matrix, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, bipedModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
                renderOverlay(matrix, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, bipedModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
            }

        });
    }


    @Override
    public void animate(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

}
