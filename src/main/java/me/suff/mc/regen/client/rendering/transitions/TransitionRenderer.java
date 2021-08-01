package me.suff.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import me.suff.mc.regen.client.animation.AnimationHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

public interface TransitionRenderer extends AnimationHandler.Animation {

    void onPlayerRenderPre(RenderPlayerEvent.Pre pre);

    void onPlayerRenderPost(RenderPlayerEvent.Post post);

    void firstPersonHand(RenderHandEvent renderHandEvent);

    void thirdPersonHand(HumanoidArm side, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    void layer(HumanoidModel<?> bipedModel, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);
}
