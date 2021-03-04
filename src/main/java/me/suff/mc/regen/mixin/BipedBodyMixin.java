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

    @Inject(at = @At("TAIL"), method = "setRotationAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void setRotationAngles(LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo) {
        AnimationHandler.setRotationAnglesCallback((BipedModel) (Object) this, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        BipedModel bipedModel = (BipedModel) (Object) this;

        if (livingEntity.getType() == EntityType.PLAYER) {
            if (livingEntity.getItemInUseCount() > 0 && (PlayerUtil.isInEitherHand(livingEntity, RItems.PISTOL.get()) || PlayerUtil.isInEitherHand(livingEntity, RItems.RIFLE.get()))) {
                bipedModel.bipedLeftArm.rotateAngleX = bipedModel.bipedHead.rotateAngleX;
                bipedModel.bipedLeftArm.rotateAngleY = bipedModel.bipedHead.rotateAngleY;
                bipedModel.bipedLeftArm.rotateAngleZ = bipedModel.bipedHead.rotateAngleZ;
                bipedModel.bipedRightArm.rotateAngleX = bipedModel.bipedHead.rotateAngleX;
                bipedModel.bipedRightArm.rotateAngleY = bipedModel.bipedHead.rotateAngleY;
                bipedModel.bipedRightArm.rotateAngleZ = bipedModel.bipedHead.rotateAngleZ;
                float aimTicks = MathHelper.clamp(livingEntity.getItemInUseCount() / 100F, 0, 1F);
                bipedModel.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-55F + aimTicks * -30F);
                bipedModel.bipedLeftArm.rotateAngleY = (float) Math.toRadians((-45F + aimTicks * -20F) * (-1));
                bipedModel.bipedRightArm.rotateAngleX = (float) Math.toRadians(-42F + aimTicks * -48F);
                bipedModel.bipedRightArm.rotateAngleY = (float) Math.toRadians((-15F + aimTicks * 5F) * (-1F));
            }
        }
    }
}
