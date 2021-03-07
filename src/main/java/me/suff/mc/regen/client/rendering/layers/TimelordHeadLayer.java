package me.suff.mc.regen.client.rendering.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.common.entities.TimelordEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class TimelordHeadLayer extends LayerRenderer< TimelordEntity, BipedModel< TimelordEntity > > {
    private static final PlayerModel< TimelordEntity > entitymodel = new PlayerModel<>(-0.25F, true);

    public TimelordHeadLayer(IEntityRenderer< TimelordEntity, BipedModel< TimelordEntity > > entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, TimelordEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        entitymodel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        entitymodel.setVisible(false);
        entitymodel.bipedHead.showModel = true;
        entitymodel.bipedHeadwear.showModel = true;
        entitymodel.bipedRightArm.showModel = true;
        entitymodel.bipedLeftArm.showModel = true;
        entitymodel.bipedRightArmwear.showModel = false;
        entitymodel.bipedLeftArmwear.showModel = false;
        entitymodel.bipedBody.showModel = true;
        entitymodel.isChild = false;
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(TimelordRenderer.getTimelordFace(entitylivingbaseIn)));
        entitymodel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (entitylivingbaseIn.getAiming()) {
            entitymodel.bipedLeftArm.rotateAngleX = entitymodel.bipedHead.rotateAngleX;
            entitymodel.bipedLeftArm.rotateAngleY = entitymodel.bipedHead.rotateAngleY;
            entitymodel.bipedLeftArm.rotateAngleZ = entitymodel.bipedHead.rotateAngleZ;
            entitymodel.bipedRightArm.rotateAngleX = entitymodel.bipedHead.rotateAngleX;
            entitymodel.bipedRightArm.rotateAngleY = entitymodel.bipedHead.rotateAngleY;
            entitymodel.bipedRightArm.rotateAngleZ = entitymodel.bipedHead.rotateAngleZ;
            float aimTicks = entitylivingbaseIn.getAimingTicks();
            entitymodel.bipedLeftArm.rotateAngleX += (float) Math.toRadians(-55F + aimTicks * -30F);
            entitymodel.bipedLeftArm.rotateAngleY += (float) Math.toRadians((-45F + aimTicks * -20F) * (-1));
            entitymodel.bipedRightArm.rotateAngleX += (float) Math.toRadians(-42F + aimTicks * -48F);
            entitymodel.bipedRightArm.rotateAngleY += (float) Math.toRadians((-15F + aimTicks * 5F) * (-1F));
        }

        entitymodel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1.0F);
    }
}
