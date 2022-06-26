package craig.software.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import craig.software.mc.regen.client.animation.AnimationHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

public interface TransitionRenderer extends AnimationHandler.Animation {

    void onPlayerRenderPre(RenderPlayerEvent.Pre pre);

    void onPlayerRenderPost(RenderPlayerEvent.Post post);

    void firstPersonHand(RenderHandEvent renderHandEvent);

    void thirdPersonHand(HumanoidArm side, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    void layer(HumanoidModel<?> bipedModel, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);
}
