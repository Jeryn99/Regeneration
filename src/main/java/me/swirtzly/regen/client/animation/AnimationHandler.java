package me.swirtzly.regen.client.animation;

import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;

public class AnimationHandler {

    public static void setRotationAnglesCallback(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            TransitionType<?> type = iRegen.getTransitionType().get();
            type.getRenderer().animate(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            correctPlayerModel(bipedModel);
        });
    }

    public static void correctPlayerModel(BipedModel bipedModel) {
        if (bipedModel instanceof PlayerModel) {
            PlayerModel playerModel = (PlayerModel) bipedModel;
            playerModel.bipedHeadwear.copyModelAngles(playerModel.bipedHead);
            playerModel.bipedLeftArmwear.copyModelAngles(playerModel.bipedLeftArm);
            playerModel.bipedRightArmwear.copyModelAngles(playerModel.bipedRightArm);
            playerModel.bipedLeftLegwear.copyModelAngles(playerModel.bipedLeftLeg);
            playerModel.bipedRightLegwear.copyModelAngles(playerModel.bipedRightLeg);
        }
    }

    public interface Animation {
        void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);
    }

}
