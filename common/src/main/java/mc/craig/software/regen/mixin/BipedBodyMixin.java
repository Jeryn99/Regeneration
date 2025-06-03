package mc.craig.software.regen.mixin;

import mc.craig.software.regen.client.animation.AnimationHandler;
import mc.craig.software.regen.client.visual.AnimationManipulation;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.util.AnimationUtil;
import mc.craig.software.regen.util.PlayerUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mc.craig.software.regen.client.animation.AnimationHandler.correctPlayerModel;

@Mixin(HumanoidModel.class)
public class BipedBodyMixin {

    @Inject(at = @At("HEAD"), cancellable = true, method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    private void setupAnimPre(LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo) {
        HumanoidModel<LivingEntity> bipedModel = (HumanoidModel) (Object) this;

        RegenerationData.get(livingEntity).ifPresent(data -> {

            // Regeneration Animation
            if (data.regenState() == RegenStates.REGENERATING && data.transitionType() == TransitionTypes.TRISTIS_IGNIS) {
                resetPlayerPositions(bipedModel);
                AnimationUtil.animate(bipedModel, data.getAnimationState(IRegen.RegenAnimation.REGEN), AnimationManipulation.REGEN, ageInTicks, 1);
                correctPlayerModel(bipedModel);
                callbackInfo.cancel();
            }

            if (data.regenState() == RegenStates.REGENERATING && data.transitionType() == TransitionTypes.DRINK) {
                resetPlayerPositions(bipedModel);
                AnimationUtil.animate(bipedModel, data.getAnimationState(IRegen.RegenAnimation.REGEN), AnimationManipulation.MCGANN_REGEN, ageInTicks, 1);
                correctPlayerModel(bipedModel);
                callbackInfo.cancel();
            }

            // "Sneeze" animation
            if (data.regenState() == RegenStates.REGENERATING && data.transitionType() == TransitionTypes.SNEEZE) {
                resetPlayerPositions(bipedModel);
                AnimationUtil.animate(bipedModel, data.getAnimationState(IRegen.RegenAnimation.REGEN), AnimationManipulation.REGEN_11_12, ageInTicks, 1);
                correctPlayerModel(bipedModel);
                callbackInfo.cancel();
            }
        });
    }

    private static void resetPlayerPositions(HumanoidModel<LivingEntity> bipedModel) {
        bipedModel.head.getAllParts().forEach(ModelPart::resetPose);
        bipedModel.hat.getAllParts().forEach(ModelPart::resetPose); // Reset second layer

        bipedModel.body.getAllParts().forEach(ModelPart::resetPose);

        bipedModel.leftArm.getAllParts().forEach(ModelPart::resetPose);

        bipedModel.rightArm.getAllParts().forEach(ModelPart::resetPose);

        bipedModel.leftLeg.getAllParts().forEach(ModelPart::resetPose);

        bipedModel.rightLeg.getAllParts().forEach(ModelPart::resetPose);

        if(bipedModel instanceof PlayerModel<LivingEntity> playerModel){
            playerModel.rightSleeve.getAllParts().forEach(ModelPart::resetPose);
            playerModel.leftSleeve.getAllParts().forEach(ModelPart::resetPose);
            playerModel.leftPants.getAllParts().forEach(ModelPart::resetPose);
            playerModel.rightPants.getAllParts().forEach(ModelPart::resetPose);
            playerModel.jacket.getAllParts().forEach(ModelPart::resetPose);
        }
    }


    @Inject(at = @At("TAIL"), cancellable = true, method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    private void setupAnim(LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo) {
        HumanoidModel<LivingEntity> bipedModel = (HumanoidModel) (Object) this;


        AnimationHandler.setRotationAnglesCallback(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        RegenerationData.get(livingEntity).ifPresent(iCap -> {
            if (iCap.regenState() == RegenStates.GRACE_CRIT) {
                bipedModel.leftArm.getAllParts().forEach(ModelPart::resetPose);
                bipedModel.rightArm.getAllParts().forEach(ModelPart::resetPose);
                AnimationUtil.animate(bipedModel, iCap.getAnimationState(IRegen.RegenAnimation.GRACE), AnimationManipulation.GRACE, ageInTicks, 1);
                correctPlayerModel(bipedModel);
                callbackInfo.cancel();
            }
        });

    }
}
