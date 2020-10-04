package me.swirtzly.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.client.animation.AnimationHandler;
import me.swirtzly.regen.client.rendering.types.RenderTypes;
import me.swirtzly.regen.common.entities.TimelordEntity;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderHandEvent;

import static me.swirtzly.regen.client.rendering.transitions.FieryTransitionRenderer.renderOverlay;
import static me.swirtzly.regen.common.regen.state.RegenStates.REGENERATING;

public class TroughtonTransitionRenderer implements TransitionRenderer {

    public static final TroughtonTransitionRenderer INSTANCE = new TroughtonTransitionRenderer();


    @Override
    public void onBefore(LivingEntity entityIn, MatrixStack matrixStackIn, float partialTicks) {

    }

    @Override
    public void onAfter(LivingEntity entityIn, MatrixStack matrixStackIn, float partialTicks) {

    }

    @Override
    public void firstPersonHand(HandSide side, IRegen iRegen, RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(HandSide side, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(BipedModel<?> bipedModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {

            float opacity = MathHelper.clamp(MathHelper.sin((entitylivingbaseIn.ticksExisted + Minecraft.getInstance().getRenderPartialTicks()) / 5) * 0.1F + 0.1F, 0.11F, 1F);

            if (iRegen.getCurrentState() == REGENERATING) {
                if (entitylivingbaseIn instanceof TimelordEntity) return;
                renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getEndPortal(1)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());
                renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getEndPortal(2)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, iRegen.getPrimaryColors());

                EntityRenderer<? super Entity> renderer = Minecraft.getInstance().getRenderManager().getRenderer(entitylivingbaseIn);
                ResourceLocation texture = renderer.getEntityTexture(entitylivingbaseIn);
                Vector3d color = new Vector3d(1, 1, 1);

                matrixStackIn.push();
                matrixStackIn.translate(0, opacity, 0);
                for (int i = 0; i < 4; i++) {
                    bipedModel.setVisible(false);
                    bipedModel.bipedHead.showModel = true;
                    bipedModel.bipedHeadwear.showModel = true;
                    switch (i) {
                        case 1:
                            matrixStackIn.push();
                            matrixStackIn.translate(1, 0, 0);
                            matrixStackIn.rotate(Vector3f.YP.rotation(opacity * 2));
                            renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getGlowing(texture)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity * 5, color);
                            matrixStackIn.pop();
                            break;
                        case 2:
                            matrixStackIn.push();
                            matrixStackIn.translate(-1, 0, 0);
                            matrixStackIn.rotate(Vector3f.YP.rotation(-opacity * 2));
                            renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getGlowing(texture)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity * 5, color);
                            matrixStackIn.pop();
                            break;
                        case 3:
                            matrixStackIn.push();
                            matrixStackIn.translate(0, -1, 0);
                            matrixStackIn.rotate(Vector3f.XP.rotation(opacity * 2));
                            renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getGlowing(texture)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity * 5, color);
                            matrixStackIn.pop();
                            break;
                        case 0:
                        case 4:
                            break;
                    }
                }
                matrixStackIn.pop();
            }
        });
    }

    @Override
    public void animation(BipedModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent((data) -> {

            if (data.getCurrentState() == REGENERATING && data.getTransitionType() == TransitionTypes.TROUGHTON) {

                bipedModel.bipedHead.rotateAngleX = (float) Math.toRadians(-20);
                bipedModel.bipedHead.rotateAngleY = (float) Math.toRadians(0);
                bipedModel.bipedHead.rotateAngleZ = (float) Math.toRadians(0);

                bipedModel.bipedLeftLeg.rotateAngleZ = (float) -Math.toRadians(5);
                bipedModel.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(5);

                bipedModel.bipedLeftArm.rotateAngleZ = (float) -Math.toRadians(5);
                bipedModel.bipedRightArm.rotateAngleZ = (float) Math.toRadians(5);
                AnimationHandler.correctPlayerModel(bipedModel);
            }
        });
    }
}
