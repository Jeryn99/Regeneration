package mc.craig.software.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.client.animation.AnimationHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface TransitionRenderer extends AnimationHandler.Animation {

    void onPlayerRenderPre(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight);

    void onPlayerRenderPost(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight);

    void firstPersonHand(InteractionHand hand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress, ItemStack stack);

    void thirdPersonHand(HumanoidArm side, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    void layer(HumanoidModel<?> bipedModel, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);
}
