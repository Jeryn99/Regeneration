package mc.craig.software.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.client.rendering.types.RenderTypes;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


public class SneezeTransitionRenderer implements TransitionRenderer {

    public static SneezeTransitionRenderer INSTANCE = new SneezeTransitionRenderer();

    @Override
    public void onPlayerRenderPre(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {

    }

    @Override
    public void onPlayerRenderPost(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        RegenerationData.get(player).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING) {
                player.yBodyRot = player.yBodyRotO = player.yHeadRot;
            }
        });
    }

    @Override
    public void firstPersonHand(InteractionHand hand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress, ItemStack stack) {

    }

    @Override
    public void thirdPersonHand(HumanoidArm side, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(HumanoidModel<?> bipedModel, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenerationData.get(entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING && entitylivingbaseIn instanceof Player) {
                float opacity = Mth.clamp(Mth.sin((entitylivingbaseIn.tickCount + Minecraft.getInstance().getFrameTime()) / 5) * 0.1F + 0.1F, 0.11F, 1F);
                FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, bipedModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
                FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, bipedModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
            }
        });
    }

    @Override
    public void animate(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

}
