package me.swirtzly.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderHandEvent;

import static me.swirtzly.regen.client.rendering.layers.RenderRegenLayer.renderCone;

public class FieryTransitionRenderer implements TransitionRenderer {

    public static final FieryTransitionRenderer INSTANCE = new FieryTransitionRenderer();

    @Override
    public void firstPersonHand(IRegen iRegen, RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
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

                    CompoundNBT colorTag = iRegen.getOrWriteStyle();
                    Vector3d primaryColors = new Vector3d(colorTag.getFloat(RConstants.PRIMARY_RED), colorTag.getFloat(RConstants.PRIMARY_GREEN), colorTag.getFloat(RConstants.PRIMARY_BLUE));
                    Vector3d secondaryColors = new Vector3d(colorTag.getFloat(RConstants.SECONDARY_RED), colorTag.getFloat(RConstants.SECONDARY_GREEN), colorTag.getFloat(RConstants.SECONDARY_BLUE));
                    renderCone(matrixStackIn, bufferIn, packedLightIn, livingEntity, primaryScale, primaryScale, primaryColors);
                    renderCone(matrixStackIn, bufferIn, packedLightIn, livingEntity, secondaryScale, secondaryScale, secondaryColors);
                }
            });
        }

    }

    @Override
    public void layer(BipedModel<?> bipedModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                // === Head Cone ===
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
                renderCone(matrixStackIn, bufferIn, packedLightIn, iRegen.getLiving(), primaryScale, primaryScale, primaryColors);
                renderCone(matrixStackIn, bufferIn, packedLightIn, iRegen.getLiving(), secondaryScale, secondaryScale, secondaryColors);
                // === End Head Cone ===
            }

        });
    }

    @Override
    public void animation(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                double arm_shake = livingEntity.getRNG().nextDouble();
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

                bipedModel.bipedLeftArm.rotateAngleZ = (float) -Math.toRadians(armRotZ + arm_shake);
                bipedModel.bipedRightArm.rotateAngleZ = (float) Math.toRadians(armRotZ + arm_shake);
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
