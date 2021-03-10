package me.suff.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.suff.mc.regen.client.animation.AnimationHandler;
import me.suff.mc.regen.client.rendering.types.RenderTypes;
import me.suff.mc.regen.common.entities.TimelordEntity;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

import static me.suff.mc.regen.client.rendering.layers.RenderRegenLayer.renderColorCone;

public class FieryTransitionRenderer implements TransitionRenderer {

    public static final FieryTransitionRenderer INSTANCE = new FieryTransitionRenderer();

    public static void renderOverlay(MatrixStack matrixStack, IVertexBuilder buffer, int packedlight, BipedModel bipedModel, LivingEntity entityPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float opacity, Vector3d color) {
        renderOverlay(matrixStack, buffer, packedlight, bipedModel, entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, color, null);
    }

    public static void renderOverlay(MatrixStack matrixStack, IVertexBuilder buffer, int packedlight, BipedModel bipedModel, LivingEntity entityPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float opacity, Vector3d color, AnimationHandler.Animation animation) {
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
    public void thirdPersonHand(HandSide side, MatrixStack matrix, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entitylivingbaseIn;
            RegenCap.get(livingEntity).ifPresent(iRegen -> {

                if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                    double x = iRegen.getTicksAnimating();
                    double p = 109.89010989010987; // see the wiki for the explanation of these "magic" numbers
                    double r = 0.09890109890109888;
                    double f = p * Math.pow(x, 2) - r;

                    float cf = MathHelper.clamp((float) f, 0F, 1F);
                    float primaryScale = cf * 4F;
                    float secondaryScale = cf * 6.4F;

                    Vector3d primaryColors = iRegen.getPrimaryColors();
                    Vector3d secondaryColors = iRegen.getSecondaryColors();
                    renderColorCone(matrix, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, livingEntity, primaryScale, primaryScale, primaryColors);
                    renderColorCone(matrix, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, livingEntity, secondaryScale, secondaryScale, secondaryColors);
                }

            });
        }

    }

    @Override
    public void layer(BipedModel< ? > bipedModel, MatrixStack matrix, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                // === Head Cone ===
                matrix.pushPose();
                bipedModel.head.translateAndRotate(matrix);
                matrix.translate(0.0f, 0.09f, 0.2f);
                matrix.mulPose(Vector3f.XP.rotation(180));
                double x = iRegen.getTicksAnimating();
                double p = 109.89010989010987; // see the wiki for the explanation of these "magic" numbers
                double r = 0.09890109890109888;
                double f = p * Math.pow(x, 2) - r;

                float cf = MathHelper.clamp((float) f, 0F, 1F);
                float primaryScale = cf * 4F;
                float secondaryScale = cf * 6.4F;

                CompoundNBT colorTag = iRegen.getOrWriteStyle();
                Vector3d primaryColors = new Vector3d(colorTag.getFloat(RConstants.PRIMARY_RED), colorTag.getFloat(RConstants.PRIMARY_GREEN), colorTag.getFloat(RConstants.PRIMARY_BLUE));
                Vector3d secondaryColors = new Vector3d(colorTag.getFloat(RConstants.SECONDARY_RED), colorTag.getFloat(RConstants.SECONDARY_GREEN), colorTag.getFloat(RConstants.SECONDARY_BLUE));
                renderColorCone(matrix, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, iRegen.getLiving(), primaryScale, primaryScale, primaryColors);
                renderColorCone(matrix, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, iRegen.getLiving(), secondaryScale, secondaryScale, secondaryColors);
                matrix.popPose();
                // === End Head Cone ===
            }

            //Render player overlay
            if (((LivingEntity) entitylivingbaseIn).hurtTime > 0 && iRegen.getCurrentState() == RegenStates.POST || iRegen.getCurrentState() == RegenStates.REGENERATING) {
                if (entitylivingbaseIn instanceof TimelordEntity) return;
                float opacity = MathHelper.clamp(MathHelper.sin((entitylivingbaseIn.tickCount + Minecraft.getInstance().getFrameTime()) / 5) * 0.1F + 0.1F, 0.11F, 1F);
                renderOverlay(matrix, bufferIn.getBuffer(RenderTypes.endPortal(1)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
                renderOverlay(matrix, bufferIn.getBuffer(RenderTypes.endPortal(2)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
            }

        });
    }


    @Override
    public void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                double armShake = livingEntity.getRandom().nextDouble();
                float armRotY = (float) iRegen.getTicksAnimating() * 1.5F;
                float armRotZ = (float) iRegen.getTicksAnimating() * 1.5F;
                float headRot = (float) iRegen.getTicksAnimating() * 1.5F;

                if (armRotY > 95) {
                    armRotY = 95;
                }

                if (armRotZ > 95) {
                    armRotZ = 95;
                }

                if (headRot > 45) {
                    headRot = 45;
                }


                // ARMS
                bipedModel.leftArm.yRot = 0;
                bipedModel.rightArm.yRot = 0;

                bipedModel.leftArm.xRot = 0;
                bipedModel.rightArm.xRot = 0;

                bipedModel.leftArm.zRot = (float) -Math.toRadians(armRotZ + armShake);
                bipedModel.rightArm.zRot = (float) Math.toRadians(armRotZ + armShake);
                bipedModel.leftArm.yRot = (float) -Math.toRadians(armRotY);
                bipedModel.rightArm.yRot = (float) Math.toRadians(armRotY);

                // BODY
                bipedModel.body.xRot = 0;
                bipedModel.body.yRot = 0;
                bipedModel.body.zRot = 0;

                // LEGS
                bipedModel.leftLeg.yRot = 0;
                bipedModel.rightLeg.yRot = 0;

                bipedModel.leftLeg.xRot = 0;
                bipedModel.rightLeg.xRot = 0;

                bipedModel.leftLeg.zRot = (float) -Math.toRadians(5);
                bipedModel.rightLeg.zRot = (float) Math.toRadians(5);

                bipedModel.head.xRot = (float) Math.toRadians(-headRot);
                bipedModel.head.yRot = (float) Math.toRadians(0);
                bipedModel.head.zRot = (float) Math.toRadians(0);

            }
        });
    }

}
