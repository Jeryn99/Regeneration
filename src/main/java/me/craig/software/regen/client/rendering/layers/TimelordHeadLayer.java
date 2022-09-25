package me.craig.software.regen.client.rendering.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.craig.software.regen.client.rendering.entity.TimelordRenderer;
import me.craig.software.regen.common.entities.TimelordEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class TimelordHeadLayer extends LayerRenderer<TimelordEntity, BipedModel<TimelordEntity>> {
    private static final PlayerModel<TimelordEntity> entitymodel = new PlayerModel<>(-0.25F, true);

    public TimelordHeadLayer(IEntityRenderer<TimelordEntity, BipedModel<TimelordEntity>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, TimelordEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        entitymodel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        entitymodel.setAllVisible(false);
        entitymodel.head.visible = true;
        entitymodel.hat.visible = true;
        entitymodel.rightArm.visible = true;
        entitymodel.leftArm.visible = true;
        entitymodel.rightSleeve.visible = false;
        entitymodel.leftSleeve.visible = false;
        entitymodel.body.visible = true;
        entitymodel.young = false;
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(TimelordRenderer.getTimelordFace(entitylivingbaseIn)));
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
    }
}
