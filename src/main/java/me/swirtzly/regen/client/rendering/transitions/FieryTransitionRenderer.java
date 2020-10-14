package me.swirtzly.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.client.animation.AnimationHandler;
import me.swirtzly.regen.client.rendering.types.RenderTypes;
import me.swirtzly.regen.common.entities.TimelordEntity;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.util.RConstants;
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

import static me.swirtzly.regen.client.rendering.layers.RenderRegenLayer.renderColorCone;

public class FieryTransitionRenderer implements TransitionRenderer {

    public static final FieryTransitionRenderer INSTANCE = new FieryTransitionRenderer();

    public static void renderOverlay(MatrixStack matrixStack, IVertexBuilder buffer, int packedlight, BipedModel bipedModel, LivingEntity entityPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float opacity, Vector3d color) {
        renderOverlay(matrixStack, buffer, packedlight, bipedModel, entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, color, null);
    }

    public static void renderOverlay(MatrixStack matrixStack, IVertexBuilder buffer, int packedlight, BipedModel bipedModel, LivingEntity entityPlayer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float opacity, Vector3d color, AnimationHandler.Animation animation) {
        RegenCap.get(entityPlayer).ifPresent((data) -> {
            matrixStack.push();
            if (animation == null) {
                bipedModel.setRotationAngles(entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            } else {
                animation.animate(bipedModel, entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            }
            bipedModel.render(matrixStack, buffer, packedlight, OverlayTexture.NO_OVERLAY, (float) color.x, (float) color.y, (float) color.z, opacity);
            matrixStack.pop();
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
    public void thirdPersonHand(HandSide side, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
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
                    renderColorCone(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, livingEntity, primaryScale, primaryScale, primaryColors);
                    renderColorCone(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, livingEntity, secondaryScale, secondaryScale, secondaryColors);
                }

            });
        }

    }

    @Override
    public void layer(BipedModel<?> bipedModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                // === Head Cone ===
                matrixStackIn.push();
                bipedModel.bipedHead.translateRotate(matrixStackIn);
                matrixStackIn.translate(0.0f, 0.09f, 0.2f);
                matrixStackIn.rotate(Vector3f.XP.rotation(180));
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
                renderColorCone(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, iRegen.getLiving(), primaryScale, primaryScale, primaryColors);
                renderColorCone(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, iRegen.getLiving(), secondaryScale, secondaryScale, secondaryColors);
                matrixStackIn.pop();
                // === End Head Cone ===
            }

            //Render player overlay
            if (((LivingEntity) entitylivingbaseIn).hurtTime > 0 && iRegen.getCurrentState() == RegenStates.POST || iRegen.getCurrentState() == RegenStates.REGENERATING) {
                if (entitylivingbaseIn instanceof TimelordEntity) return;
                float opacity = MathHelper.clamp(MathHelper.sin((entitylivingbaseIn.ticksExisted + Minecraft.getInstance().getRenderPartialTicks()) / 5) * 0.1F + 0.1F, 0.11F, 1F);
                renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getEndPortal(1)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
                renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getEndPortal(2)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
            }

        });
    }


    @Override
    public void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                double armShake = livingEntity.getRNG().nextDouble();
                float armRotY = (float) iRegen.getTicksAnimating() * 1.5F;
                float armRotZ = (float) iRegen.getTicksAnimating() * 1.5F;
                float headRot = (float) iRegen.getTicksAnimating() * 1.5F;

                if (armRotY > 90) {
                    armRotY = 90;
                }

                if (armRotZ > 95) {
                    armRotZ = 95;
                }

                if (headRot > 45) {
                    headRot = 45;
                }


                // ARMS
                bipedModel.bipedLeftArm.rotateAngleY = 0;
                bipedModel.bipedRightArm.rotateAngleY = 0;

                bipedModel.bipedLeftArm.rotateAngleX = 0;
                bipedModel.bipedRightArm.rotateAngleX = 0;

                bipedModel.bipedLeftArm.rotateAngleZ = (float) -Math.toRadians(armRotZ + armShake);
                bipedModel.bipedRightArm.rotateAngleZ = (float) Math.toRadians(armRotZ + armShake);
                bipedModel.bipedLeftArm.rotateAngleY = (float) -Math.toRadians(armRotY);
                bipedModel.bipedRightArm.rotateAngleY = (float) Math.toRadians(armRotY);

                // BODY
                bipedModel.bipedBody.rotateAngleX = 0;
                bipedModel.bipedBody.rotateAngleY = 0;
                bipedModel.bipedBody.rotateAngleZ = 0;

                // LEGS
                bipedModel.bipedLeftLeg.rotateAngleY = 0;
                bipedModel.bipedRightLeg.rotateAngleY = 0;

                bipedModel.bipedLeftLeg.rotateAngleX = 0;
                bipedModel.bipedRightLeg.rotateAngleX = 0;

                bipedModel.bipedLeftLeg.rotateAngleZ = (float) -Math.toRadians(5);
                bipedModel.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(5);

                bipedModel.bipedHead.rotateAngleX = (float) Math.toRadians(-headRot);
                bipedModel.bipedHead.rotateAngleY = (float) Math.toRadians(0);
                bipedModel.bipedHead.rotateAngleZ = (float) Math.toRadians(0);

            }
        });
    }

}
