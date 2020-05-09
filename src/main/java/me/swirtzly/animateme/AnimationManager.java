package me.swirtzly.animateme;

import me.swirtzly.regeneration.Regeneration;
import net.minecraft.client.renderer.entity.LivingRenderer;
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
        Regeneration.LOG.info("Registered Animation Handler {}", animation.getClass().getName());
    }

    public static void registerAnimations(IAnimate... animations) {
        for (IAnimate animation : animations) {
            ANIMATIONS.add(animation);
            Regeneration.LOG.info("Registered Animation Handler {}", animation.getClass().getName());
        }
    }

    public interface IAnimate {

        void preRenderCallBack(LivingRenderer renderer, LivingEntity entity);

        void preAnimation(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

        void postAnimation(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

        boolean useVanilla();
    }
}
