package mc.craig.software.regen.mixin;

import mc.craig.software.regen.client.visual.AnimationManipulator;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

    @Inject(at = @At("TAIL"), method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", cancellable = true)
    private void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        HumanoidModel<LivingEntity> humanoidModel = (HumanoidModel<LivingEntity>) (Object) this;
        AnimationManipulator.animatePlayer(humanoidModel, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, ci);
    }
}
