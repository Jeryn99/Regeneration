package me.swirtzly.regen.mixin;

import me.swirtzly.regen.client.animation.AnimationHandler;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedModel.class)
public class BipedBodyMixin {

    @Inject(at = @At("TAIL"), method = "setRotationAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void setRotationAngles(LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo){
        AnimationHandler.setRotationAnglesCallback((BipedModel) (Object) this, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}
