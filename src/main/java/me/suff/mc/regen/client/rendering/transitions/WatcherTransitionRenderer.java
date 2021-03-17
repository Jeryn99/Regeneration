package me.suff.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.suff.mc.regen.client.animation.AnimationHandler;
import me.suff.mc.regen.client.rendering.types.RenderTypes;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class WatcherTransitionRenderer implements TransitionRenderer {

    public static final WatcherTransitionRenderer INSTANCE = new WatcherTransitionRenderer();
    private static final ResourceLocation TEXTURE = new ResourceLocation(RConstants.MODID, "textures/entity/watcher.png");


    @Override
    public void onPlayerRenderPre(RenderPlayerEvent.Pre pre) {
        PlayerEntity player = pre.getPlayer();

        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                MatrixStack maxtrix = pre.getMatrixStack();
                player.yBodyRot = player.yBodyRotO = 0;
                maxtrix.translate(0, 0.1, 0);
                maxtrix.mulPose(Vector3f.XN.rotationDegrees(90));
            }
        });
    }

    @Override
    public void onPlayerRenderPost(RenderPlayerEvent.Post post) {
        PlayerEntity player = post.getPlayer();
        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                player.yBodyRot = player.yBodyRotO = player.yRot;
            }
        });
    }

    @Override
    public void firstPersonHand(RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(HandSide side, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(BipedModel< ? > bipedModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                matrixStackIn.pushPose();
                matrixStackIn.scale(1.2F, 1.2F, 1.2F);
                float opacity = iRegen.updateTicks();
                FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.entityTranslucent(TEXTURE)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, new Vector3d(1, 1, 1));
                matrixStackIn.popPose();

                if (iRegen.updateTicks() < 150) {
                    matrixStackIn.pushPose();
                    matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(-90));
                    matrixStackIn.translate(0, -1.5, 1);
                    FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.entityTranslucent(TEXTURE)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.5F, new Vector3d(1, 1, 1), new AnimationHandler.Animation() {
                        @Override
                        public void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
                            bipedModel.head.xRot = (float) Math.toRadians(40);
                            bipedModel.hat.xRot = (float) Math.toRadians(40);
                        }
                    });
                    matrixStackIn.popPose();
                }
            }
        });

    }

    @Override
    public void animate(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent((data) -> {
            if (data.getCurrentState() == RegenStates.REGENERATING && data.getTransitionType() == TransitionTypes.WATCHER) {
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
