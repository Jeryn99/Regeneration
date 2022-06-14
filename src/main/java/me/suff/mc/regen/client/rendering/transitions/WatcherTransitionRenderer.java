package me.suff.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.suff.mc.regen.client.rendering.types.RenderTypes;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class WatcherTransitionRenderer implements TransitionRenderer {

    public static final WatcherTransitionRenderer INSTANCE = new WatcherTransitionRenderer();
    private static final ResourceLocation TEXTURE = new ResourceLocation(RConstants.MODID, "textures/entity/watcher.png");


    @Override
    public void onPlayerRenderPre(RenderPlayerEvent.Pre pre) {
        Player player = pre.getPlayer();

        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING) {
                PoseStack maxtrix = pre.getPoseStack();
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
                player.yBodyRot = player.yBodyRotO = player.yHeadRot;
            }
        });
    }

    @Override
    public void firstPersonHand(RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(HumanoidArm side, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(HumanoidModel<?> bipedModel, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING) {
                matrixStackIn.pushPose();
                matrixStackIn.scale(1.2F, 1.2F, 1.2F);
                float opacity = iRegen.updateTicks();
                FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.entityTranslucent(TEXTURE)), packedLightIn, bipedModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, new Vec3(1, 1, 1));
                matrixStackIn.popPose();

                if (iRegen.updateTicks() < 150) {
                    matrixStackIn.pushPose();
                    matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(-90));
                    matrixStackIn.translate(0, -1.5, 1);
                    FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.entityTranslucent(TEXTURE)), packedLightIn, bipedModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.5F, new Vec3(1, 1, 1), (bipedModel1, livingEntity, limbSwing1, limbSwingAmount1, ageInTicks1, netHeadYaw1, headPitch1) -> {
                        bipedModel1.head.xRot = (float) Math.toRadians(40);
                        bipedModel1.hat.xRot = (float) Math.toRadians(40);
                    });
                    matrixStackIn.popPose();
                }
            }
        });

    }

    @Override
    public void animate(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent((data) -> {
            if (data.regenState() == RegenStates.REGENERATING && data.transitionType() == TransitionTypes.WATCHER.get()) {
                bipedModel.head.xRot = (float) Math.toRadians(0);
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
