package mc.craig.software.regen.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class AnimationManipulator {

    public static void animatePlayer(HumanoidModel<LivingEntity> humanoidModel, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        humanoidModel.head.xRot = (float) Math.toRadians(90);

    }

}
