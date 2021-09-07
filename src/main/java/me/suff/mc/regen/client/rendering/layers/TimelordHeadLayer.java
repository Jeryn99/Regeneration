package me.suff.mc.regen.client.rendering.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.common.entities.TimelordEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class TimelordHeadLayer extends RenderLayer<TimelordEntity, PlayerModel<TimelordEntity>> {
    private static final PlayerModel<TimelordEntity> alexPlayerModel = new PlayerModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM), true);

    public TimelordHeadLayer(RenderLayerParent<TimelordEntity, PlayerModel<TimelordEntity>> renderLayerParent) {
        super(renderLayerParent);
    }


    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, TimelordEntity timelordEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        alexPlayerModel.prepareMobModel(timelordEntity, limbSwing, limbSwingAmount, partialTicks);
        alexPlayerModel.setAllVisible(false);
        alexPlayerModel.head.visible = true;
        alexPlayerModel.hat.visible = true;
        alexPlayerModel.rightArm.visible = true;
        alexPlayerModel.leftArm.visible = true;
        alexPlayerModel.rightSleeve.visible = false;
        alexPlayerModel.leftSleeve.visible = false;
        alexPlayerModel.body.visible = true;
        alexPlayerModel.young = false;
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(TimelordRenderer.getTimelordFace(timelordEntity)));
        alexPlayerModel.setupAnim(timelordEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (timelordEntity.getAiming()) {
            alexPlayerModel.leftArm.xRot = alexPlayerModel.head.xRot;
            alexPlayerModel.leftArm.yRot = alexPlayerModel.head.yRot;
            alexPlayerModel.leftArm.zRot = alexPlayerModel.head.zRot;
            alexPlayerModel.rightArm.xRot = alexPlayerModel.head.xRot;
            alexPlayerModel.rightArm.yRot = alexPlayerModel.head.yRot;
            alexPlayerModel.rightArm.zRot = alexPlayerModel.head.zRot;
            float aimTicks = timelordEntity.getAimingTicks();
            alexPlayerModel.leftArm.xRot += (float) Math.toRadians(-55F + aimTicks * -30F);
            alexPlayerModel.leftArm.yRot += (float) Math.toRadians((-45F + aimTicks * -20F) * (-1));
            alexPlayerModel.rightArm.xRot += (float) Math.toRadians(-42F + aimTicks * -48F);
            alexPlayerModel.rightArm.yRot += (float) Math.toRadians((-15F + aimTicks * 5F) * (-1F));
        }

        alexPlayerModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1.0F);

    }
}
