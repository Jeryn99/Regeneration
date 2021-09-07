package me.suff.mc.regen.client.rendering.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.common.entities.TimelordEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class TimelordHeadLayer extends RenderLayer<TimelordEntity, PlayerModel<TimelordEntity>> {
    private static ModelPart entitymodel = null;

    public TimelordHeadLayer(RenderLayerParent<TimelordEntity, PlayerModel<TimelordEntity>> p_117346_) {
        super(p_117346_);
        entitymodel = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM);
    }


    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, TimelordEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        /*entitymodel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        entitymodel.setAllVisible(false);
        entitymodel.head.visible = true;
        entitymodel.hat.visible = true;
        entitymodel.rightArm.visible = true;
        entitymodel.leftArm.visible = true;
        entitymodel.rightSleeve.visible = false;
        entitymodel.leftSleeve.visible = false;
        entitymodel.body.visible = true;
        entitymodel.young = false;
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(TimelordRenderer.getTimelordFace(entitylivingbaseIn)));
        entitymodel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (entitylivingbaseIn.getAiming()) {
            entitymodel.leftArm.xRot = entitymodel.head.xRot;
            entitymodel.leftArm.yRot = entitymodel.head.yRot;
            entitymodel.leftArm.zRot = entitymodel.head.zRot;
            entitymodel.rightArm.xRot = entitymodel.head.xRot;
            entitymodel.rightArm.yRot = entitymodel.head.yRot;
            entitymodel.rightArm.zRot = entitymodel.head.zRot;
            float aimTicks = entitylivingbaseIn.getAimingTicks();
            entitymodel.leftArm.xRot += (float) Math.toRadians(-55F + aimTicks * -30F);
            entitymodel.leftArm.yRot += (float) Math.toRadians((-45F + aimTicks * -20F) * (-1));
            entitymodel.rightArm.xRot += (float) Math.toRadians(-42F + aimTicks * -48F);
            entitymodel.rightArm.yRot += (float) Math.toRadians((-15F + aimTicks * 5F) * (-1F));
        }

        entitymodel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1.0F);
    */
    }    //TODO
}
