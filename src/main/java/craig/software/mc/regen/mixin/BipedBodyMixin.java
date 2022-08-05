package craig.software.mc.regen.mixin;

import craig.software.mc.regen.client.RegenAnimations;
import craig.software.mc.regen.client.animation.AnimationHandler;
import craig.software.mc.regen.common.objects.RItems;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.regen.state.RegenStates;
import craig.software.mc.regen.common.regen.transitions.TransitionType;
import craig.software.mc.regen.common.regen.transitions.TransitionTypes;
import craig.software.mc.regen.util.AnimationUtil;
import craig.software.mc.regen.util.PlayerUtil;
import craig.software.mc.regen.util.RegenUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static craig.software.mc.regen.client.animation.AnimationHandler.correctPlayerModel;

@Mixin(HumanoidModel.class)
public class BipedBodyMixin {

    @Inject(at = @At("HEAD"), cancellable = true, method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    private void setupAnimPre(LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo) {
        HumanoidModel<LivingEntity> bipedModel = (HumanoidModel) (Object) this;
        RegenCap.get(livingEntity).ifPresent(iCap -> {
            // Regeneration Animation
            if (iCap.regenState() == RegenStates.REGENERATING && iCap.transitionType() == TransitionTypes.TRISTIS_IGNIS.get()) {
                AnimationUtil.animate(bipedModel, iCap.getAnimationState(), RegenAnimations.REGEN, ageInTicks, 1);
                correctPlayerModel(bipedModel);

                callbackInfo.cancel();
            }
        });
    }

    @Inject(at = @At("TAIL"), method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    private void setupAnim(LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo) {
        HumanoidModel<LivingEntity> bipedModel = (HumanoidModel) (Object) this;

        AnimationHandler.setRotationAnglesCallback(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);


        if (livingEntity.getType() == EntityType.PLAYER) {
            if (livingEntity.getUseItemRemainingTicks() > 0 && (PlayerUtil.isInEitherHand(livingEntity, RItems.PISTOL.get()) || PlayerUtil.isInEitherHand(livingEntity, RItems.RIFLE.get()))) {
                bipedModel.leftArm.xRot = bipedModel.head.xRot;
                bipedModel.leftArm.yRot = bipedModel.head.yRot;
                bipedModel.leftArm.zRot = bipedModel.head.zRot;
                bipedModel.rightArm.xRot = bipedModel.head.xRot;
                bipedModel.rightArm.yRot = bipedModel.head.yRot;
                bipedModel.rightArm.zRot = bipedModel.head.zRot;
                float aimTicks = Mth.clamp(livingEntity.getUseItemRemainingTicks() / 100F, 0, 1F);
                bipedModel.leftArm.xRot += (float) Math.toRadians(-55F + aimTicks * -30F);
                bipedModel.leftArm.yRot += (float) Math.toRadians((-45F + aimTicks * -20F) * (-1));
                bipedModel.rightArm.xRot += (float) Math.toRadians(-42F + aimTicks * -48F);
                bipedModel.rightArm.yRot += (float) Math.toRadians((-15F + aimTicks * 5F) * (-1F));
            }
        }
    }
}
