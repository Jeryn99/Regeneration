package me.swirtzly.animateme;

import me.swirtzly.regeneration.RegenerationMod;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager {

    private static List<IAnimate> ANIMATIONS = new ArrayList();

    public static List<IAnimate> getAnimations() {
        return ANIMATIONS;
    }

    public static void registerAnimation(IAnimate animation) {
        ANIMATIONS.add(animation);
        RegenerationMod.LOG.info("Registered Animation Handler {}", animation.getClass().getName());
    }

    public interface IAnimate {
        BipedModel preAnimation(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

        BipedModel postAnimation(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

        boolean useVanilla();
    }
}
