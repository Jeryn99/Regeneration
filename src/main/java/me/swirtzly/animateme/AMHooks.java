package me.swirtzly.animateme;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.types.TypeManager;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static me.swirtzly.regeneration.client.animation.GeneralAnimations.copyAnglesToWear;

public class AMHooks {

    /**
     * This is fired directly before setRotationAngles in BipedModel, you should NOT call this yourself.
     * You should instead Subscribe to the event as needed.
     */
    public static void renderBipedPre(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
       // for (AnimationManager.IAnimate animation : AnimationManager.getAnimations()) {
      //      if (animation.useVanilla()) {
      //          model.setRotationAngles(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
      //      } else {
      //          animation.preAnimation(model, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
      //      }
      //  }
    }

    /**
     * This is fired directly after setRotationAngles in BipedModel, you should NOT call this yourself.
     * You should instead Subscribe to the event as needed.
     */
    public static void renderBipedPost(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        for (AnimationManager.IAnimate animation : AnimationManager.getAnimations()) {
            animation.postAnimation(model, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }

        AtomicInteger animationTicks = new AtomicInteger();
        AtomicBoolean isFieryAndRegenerating = new AtomicBoolean(false);
        RegenCap.getForPlayer(entity).ifPresent((data) -> animationTicks.set(data.getAnimationTicks()));
        RegenCap.getForPlayer(entity).ifPresent((data) -> isFieryAndRegenerating.set(data.getState() == PlayerUtil.RegenState.REGENERATING && data.getType() == TypeManager.Type.FIERY));

        if (isFieryAndRegenerating.get()) {


            double arm_shake = entity.getRNG().nextDouble();
            float armRot = (float) animationTicks.get() * 1.5F;
            float headRot = (float) animationTicks.get() * 0.5F;

            if (armRot > 90) {
                armRot = 90;
            }

            if (headRot > 45) {
                headRot = 45;
            }

            //ARMS
            model.bipedLeftArm.rotateAngleY = 0;
            model.bipedRightArm.rotateAngleY = 0;

            model.bipedLeftArm.rotateAngleX = 0;
            model.bipedRightArm.rotateAngleX = 0;

            model.bipedLeftArm.rotateAngleZ = (float) -Math.toRadians(armRot + arm_shake);
            model.bipedRightArm.rotateAngleZ = (float) Math.toRadians(armRot + arm_shake);

            //BODY
            model.bipedBody.rotateAngleX = 0;
            model.bipedBody.rotateAngleY = 0;
            model.bipedBody.rotateAngleZ = 0;


            //LEGS
            model.bipedLeftLeg.rotateAngleY = 0;
            model.bipedRightLeg.rotateAngleY = 0;

            model.bipedLeftLeg.rotateAngleX = 0;
            model.bipedRightLeg.rotateAngleX = 0;

            model.bipedLeftLeg.rotateAngleZ = (float) -Math.toRadians(5);
            model.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(5);

            model.bipedHead.rotateAngleX = (float) Math.toRadians(-headRot);

            copyAnglesToWear(model);


        }

    }


}
