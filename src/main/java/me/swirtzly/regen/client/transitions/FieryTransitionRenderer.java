package me.swirtzly.regen.client.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.common.regen.IRegen;
import net.minecraft.client.gui.overlay.SubtitleOverlayGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.client.event.RenderHandEvent;

public class FieryTransitionRenderer implements TransitionRenderer {

    public static final FieryTransitionRenderer INSTANCE = new FieryTransitionRenderer();

    @Override
    public void firstPersonHand(IRegen iRegen, RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void animation(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //TODO Reimplement animationTicks
        double arm_shake = livingEntity.getRNG().nextDouble();
        float armRotY = (float) livingEntity.ticksExisted * 1.5F;
        float armRotZ = (float) livingEntity.ticksExisted * 1.5F;
        float headRot = (float) livingEntity.ticksExisted * 1.5F;

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
}
