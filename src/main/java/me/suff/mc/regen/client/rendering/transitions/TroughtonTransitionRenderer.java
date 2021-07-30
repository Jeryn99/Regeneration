package me.suff.mc.regen.client.rendering.transitions;

import com.mojang.blaze3d.vertex.PoseStack;
import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.client.rendering.types.RenderTypes;
import me.suff.mc.regen.common.entities.TimelordEntity;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class TroughtonTransitionRenderer implements TransitionRenderer {

    public static final TroughtonTransitionRenderer INSTANCE = new TroughtonTransitionRenderer();

    @Override
    public void onPlayerRenderPre(RenderPlayerEvent.Pre pre) {

    }

    @Override
    public void onPlayerRenderPost(RenderPlayerEvent.Post post) {

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


            float opacity = Mth.clamp(Mth.sin((entitylivingbaseIn.tickCount + Minecraft.getInstance().getFrameTime()) / 5) * 0.1F + 0.1F, 0.11F, 1F);

            if (iRegen.regenState() == RegenStates.REGENERATING) {

                EntityRenderer<? super Entity> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entitylivingbaseIn);
                PlayerRenderer playerRenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(Minecraft.getInstance().player);
                ResourceLocation headTexture = entityRenderer.getTextureLocation(entitylivingbaseIn);

                if (entitylivingbaseIn instanceof TimelordEntity) {
                    TimelordEntity timelordEntity = (TimelordEntity) entitylivingbaseIn;
                    headTexture = TimelordRenderer.getTimelordFace(timelordEntity);
                }
                Vec3 color = new Vec3(1, 1, 1);
                PlayerModel<AbstractClientPlayer> headModel = playerRenderer.getModel();

                matrixStackIn.pushPose();
                matrixStackIn.translate(0, opacity, 0);
                for (int i = 0; i < 4; i++) {
                    headModel.setAllVisible(false);
                    headModel.head.visible = true;
                    headModel.hat.visible = true;
                    switch (i) {
                        case 1:
                            matrixStackIn.pushPose();
                            matrixStackIn.translate(1, 0, 0);
                            matrixStackIn.mulPose(Vector3f.YP.rotation(opacity * 2));
                            FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getGlowing(headTexture)), packedLightIn, headModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity * 5, color);
                            matrixStackIn.popPose();
                            break;
                        case 2:
                            matrixStackIn.pushPose();
                            matrixStackIn.translate(-1, 0, 0);
                            matrixStackIn.mulPose(Vector3f.YP.rotation(-opacity * 2));
                            FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getGlowing(headTexture)), packedLightIn, headModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity * 5, color);
                            matrixStackIn.popPose();
                            break;
                        case 3:
                            matrixStackIn.pushPose();
                            matrixStackIn.translate(0, -1, 0);
                            matrixStackIn.mulPose(Vector3f.XP.rotation(opacity * 2));
                            FieryTransitionRenderer.renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getGlowing(headTexture)), packedLightIn, headModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity * 5, color);
                            matrixStackIn.popPose();
                            break;
                        case 0:
                        case 4:
                            break;
                    }
                }
                matrixStackIn.popPose();
            }
        });
    }

    @Override
    public void animate(HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent((data) -> {

            if (data.regenState() == RegenStates.REGENERATING && data.transitionType() == TransitionTypes.TROUGHTON.get()) {

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
