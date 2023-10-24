package mc.craig.software.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mc.craig.software.regen.client.rendering.entity.TimelordRenderer;
import mc.craig.software.regen.common.entities.Timelord;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TroughtonTransitionRenderer implements TransitionRenderer {

    public static final TroughtonTransitionRenderer INSTANCE = new TroughtonTransitionRenderer();


    @Override
    public void onPlayerRenderPre(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {

    }

    @Override
    public void onPlayerRenderPost(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {

    }

    @Override
    public void firstPersonHand(InteractionHand hand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTick, float swingProgress, float equipProgress, ItemStack stack) {

    }


    @Override
    public void thirdPersonHand(HumanoidArm side, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(HumanoidModel<?> bipedModel, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenerationData.get(entitylivingbaseIn).ifPresent(iRegen -> {


            float opacity = Mth.clamp(Mth.sin((entitylivingbaseIn.tickCount + Minecraft.getInstance().getFrameTime()) / 5) * 0.1F + 0.1F, 0.11F, 1F);

            if (iRegen.regenState() == RegenStates.REGENERATING) {

                EntityRenderer<? super Entity> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entitylivingbaseIn);
                PlayerRenderer playerRenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(Minecraft.getInstance().player);
                ResourceLocation headTexture = entityRenderer.getTextureLocation(entitylivingbaseIn);

                if (entitylivingbaseIn instanceof Timelord timelord) {
                    headTexture = TimelordRenderer.getTimelordTexture(timelord);
                }

                Vec3 color = new Vec3(1, 1, 1);
                PlayerModel<AbstractClientPlayer> headModel = playerRenderer.getModel();

                float rotate = ((float) entitylivingbaseIn.tickCount + partialTicks) * 3.0F;


                matrixStackIn.pushPose();
                matrixStackIn.mulPose(Axis.YN.rotation(rotate / 50)); //
                matrixStackIn.translate(0, -0.2, 0);
                matrixStackIn.translate(0, opacity, 0);

                headModel.setAllVisible(false);
                headModel.head.visible = true;
                headModel.hat.visible = true;
                headModel.head.xRot = 0;
                headModel.head.yRot = 0;
                headModel.head.zRot = 0;
                for (int i = 0; i < 5; i++) {
                    switch (i) {
                        case 1:
                            matrixStackIn.pushPose();
                            matrixStackIn.translate(1, 0, 0);
                            matrixStackIn.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                            FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutout(headTexture)), packedLightIn, headModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 100, color);
                            matrixStackIn.popPose();
                            break;
                        case 2:
                            matrixStackIn.pushPose();
                            matrixStackIn.translate(-1, 0, 0);
                            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-180));
                            matrixStackIn.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                            FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutout(headTexture)), packedLightIn, headModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 100, color);
                            matrixStackIn.popPose();
                            break;
                        case 3:
                            matrixStackIn.pushPose();
                            matrixStackIn.translate(0, 0, -1);
                            matrixStackIn.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                            matrixStackIn.mulPose(Axis.YP.rotationDegrees(90));
                            FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutout(headTexture)), packedLightIn, headModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 100, color);
                            matrixStackIn.popPose();
                            break;
                        case 4:
                            matrixStackIn.pushPose();
                            matrixStackIn.translate(0, 0, 1);
                            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-90));
                            matrixStackIn.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                            FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutout(headTexture)), packedLightIn, headModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 100, color);
                            matrixStackIn.popPose();
                        case 0:
                            break;
                    }
                }
                matrixStackIn.popPose();
            }
        });
    }

    @Override
    public void animate(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenerationData.get(livingEntity).ifPresent((data) -> {

            if (data.regenState() == RegenStates.REGENERATING && data.transitionType() == TransitionTypes.TROUGHTON) {

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
