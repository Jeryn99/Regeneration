package me.swirtzly.regen.client.animation;

import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionType;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class AnimationHandler {

    public static void setRotationAnglesCallback(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            TransitionType type = iRegen.getTransitionType().create();
            type.getRenderer().animation(bipedModel, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        });
    }

}
