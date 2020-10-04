package me.swirtzly.regen.client.rendering.transitions;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.client.animation.AnimationHandler;
import me.swirtzly.regen.client.rendering.types.RenderTypes;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderHandEvent;

import static me.swirtzly.regen.client.rendering.transitions.FieryTransitionRenderer.renderOverlay;
import static me.swirtzly.regen.common.regen.state.RegenStates.REGENERATING;

public class WatcherTransitionRenderer implements TransitionRenderer {

    public static final WatcherTransitionRenderer INSTANCE = new WatcherTransitionRenderer();
    private static final ResourceLocation texture = new ResourceLocation(RConstants.MODID, "textures/entity/watcher.png");

    @Override
    public void onBefore(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

    }

    @Override
    public void firstPersonHand(RenderHandEvent renderHandEvent) {

    }

    @Override
    public void thirdPersonHand(HandSide side, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void layer(BipedModel<?> bipedModel, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.REGENERATING) {
                matrixStackIn.push();
                matrixStackIn.scale(1.2F, 1.2F, 1.2F);
                float opacity = iRegen.getTicksAnimating();
                renderOverlay(matrixStackIn, bufferIn.getBuffer(RenderTypes.getEntityTranslucent(texture)), packedLightIn, bipedModel, (LivingEntity) entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, opacity, new Vector3d(1, 1, 1));
                matrixStackIn.pop();
            }
        });
    }

    @Override
    public void animation(BipedModel<?> bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        RegenCap.get(livingEntity).ifPresent((data) -> {
            if (data.getCurrentState() == REGENERATING && data.getTransitionType() == TransitionTypes.WATCHER) {
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
