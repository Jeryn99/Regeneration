package me.suff.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.suff.mc.regen.client.rendering.types.RenderTypes;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

import static me.suff.mc.regen.client.rendering.transitions.FieryTransitionRenderer.renderOverlay;

public class SparkleTransitionRenderer implements TransitionRenderer {

    public static SparkleTransitionRenderer INSTANCE = new SparkleTransitionRenderer();

    @Override
    public void onPlayerRenderPre(RenderPlayerEvent.Pre pre) {
        Player player = pre.getPlayer();

        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING) {
                PoseStack maxtrix = pre.getMatrixStack();
                player.yBodyRot = player.yBodyRotO = 0;
                maxtrix.translate(0, 0.1, 0);
                maxtrix.mulPose(Vector3f.XN.rotationDegrees(90));
            }
        });
    }

    @Override
    public void onPlayerRenderPost(RenderPlayerEvent.Post post) {
        Player player = post.getPlayer();
        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING) {
                player.yBodyRot = player.yBodyRotO = player.yRot;
            }
        });
    }

    @Override
    public void firstPersonHand(RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(HumanoidArm side, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(HumanoidModel<?> bipedModel, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING) {
                float opacity = Mth.clamp(Mth.sin((entitylivingbaseIn.tickCount + Minecraft.getInstance().getFrameTime()) / 5) * 0.1F + 0.1F, 0.11F, 1F);
                renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.endPortal(1)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
                renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.endPortal(2)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
            }
        });
    }

    @Override
    public void animate(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent((data) -> {
            if (data.regenState() == RegenStates.REGENERATING && data.transitionType() == TransitionTypes.SPARKLE.get()) {
                bipedModel.head.xRot = (float) Math.toRadians(-20);
                bipedModel.head.yRot = (float) Math.toRadians(0);
                bipedModel.head.zRot = (float) Math.toRadians(0);

                bipedModel.leftLeg.zRot = (float) -Math.toRadians(5);
                bipedModel.rightLeg.zRot = (float) Math.toRadians(5);

                bipedModel.leftArm.zRot = (float) -Math.toRadians(5);
                bipedModel.rightArm.zRot = (float) Math.toRadians(5);
            }
        });
    }

}
