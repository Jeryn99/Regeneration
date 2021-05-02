package me.suff.mc.regen.mixin;

import me.suff.mc.regen.client.animation.AnimationHandler;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedModel.class)
public class BipedBodyMixin {

    @Inject(at = @At("TAIL"), method = "setupAnim(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void setupAnim(LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo) {
        BipedModel bipedModel = (BipedModel) (Object) this;

        AnimationHandler.setRotationAnglesCallback(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);


        if (livingEntity.getType() == EntityType.PLAYER) {
            if (livingEntity.getUseItemRemainingTicks() > 0 && (PlayerUtil.isInEitherHand(livingEntity, RItems.PISTOL.get()) || PlayerUtil.isInEitherHand(livingEntity, RItems.RIFLE.get()))) {
                bipedModel.leftArm.xRot = bipedModel.head.xRot;
                bipedModel.leftArm.yRot = bipedModel.head.yRot;
                bipedModel.leftArm.zRot = bipedModel.head.zRot;
                bipedModel.rightArm.xRot = bipedModel.head.xRot;
                bipedModel.rightArm.yRot = bipedModel.head.yRot;
                bipedModel.rightArm.zRot = bipedModel.head.zRot;
                float aimTicks = MathHelper.clamp(livingEntity.getUseItemRemainingTicks() / 100F, 0, 1F);
                bipedModel.leftArm.xRot += (float) Math.toRadians(-55F + aimTicks * -30F);
                bipedModel.leftArm.yRot += (float) Math.toRadians((-45F + aimTicks * -20F) * (-1));
                bipedModel.rightArm.xRot += (float) Math.toRadians(-42F + aimTicks * -48F);
                bipedModel.rightArm.yRot += (float) Math.toRadians((-15F + aimTicks * 5F) * (-1F));
            }
        }
    }
}
