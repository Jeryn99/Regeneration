package me.swirtzly.animateme;

import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.RegenUtil;
import me.swirtzly.regeneration.util.RenderUtil;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;

public class AMHooks {

    /**
     * This is fired directly before setRotationAngles in BipedModel, you should NOT call this yourself.
     * You should instead Subscribe to the event as needed.
     */
    public static void renderBipedPre(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
       // for (AnimationManager.IAnimate animation : AnimationManager.getAnimations()) {
      //      if (animation.useVanilla()) {
      //          model.setRotationAngles(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
      //      } else {
      //          animation.preAnimation(model, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
      //      }
      //  }
    }

    /**
     * This is fired directly after setRotationAngles in BipedModel, you should NOT call this yourself.
     * You should instead Subscribe to the event as needed.
     */
    public static void renderBipedPost(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        for (AnimationManager.IAnimate animation : AnimationManager.getAnimations()) {
            BipedModel oldmodel = animation.postAnimation(model, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            for (int i = 0; i < oldmodel.boxList.size(); i++) {
                RendererModel render = oldmodel.boxList.get(i);
                RenderUtil.copyModelAngles(render, model.boxList.get(i));
            }
        }
    }

}
