package me.suff.mc.regen.client.rendering.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.client.rendering.model.ModifiedPlayerModel;
import me.suff.mc.regen.client.rendering.model.RModels;
import me.suff.mc.regen.common.entities.Timelord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class TimelordHeadLayer extends RenderLayer<Timelord, PlayerModel<Timelord>> {
    private static final ModifiedPlayerModel alexPlayerModel = new ModifiedPlayerModel(Minecraft.getInstance().getEntityModels().bakeLayer(RModels.MOD_PLAYER), true);

    public TimelordHeadLayer(RenderLayerParent<Timelord, PlayerModel<Timelord>> renderLayerParent) {
        super(renderLayerParent);
    }


    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Timelord timelord, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        alexPlayerModel.prepareMobModel(timelord, limbSwing, limbSwingAmount, partialTicks);
        alexPlayerModel.setAllVisible(false);
        alexPlayerModel.head.visible = true;
        alexPlayerModel.hat.visible = true;
        alexPlayerModel.rightArm.visible = true;
        alexPlayerModel.leftArm.visible = true;
        alexPlayerModel.rightSleeve.visible = false;
        alexPlayerModel.leftSleeve.visible = false;
        alexPlayerModel.body.visible = true;
        alexPlayerModel.young = false;
        VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.entityTranslucent(TimelordRenderer.getTimelordFace(timelord)));
        alexPlayerModel.setupAnim(timelord, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (timelord.getAiming()) {
            alexPlayerModel.leftArm.xRot = alexPlayerModel.head.xRot;
            alexPlayerModel.leftArm.yRot = alexPlayerModel.head.yRot;
            alexPlayerModel.leftArm.zRot = alexPlayerModel.head.zRot;
            alexPlayerModel.rightArm.xRot = alexPlayerModel.head.xRot;
            alexPlayerModel.rightArm.yRot = alexPlayerModel.head.yRot;
            alexPlayerModel.rightArm.zRot = alexPlayerModel.head.zRot;
            float aimTicks = timelord.getAimingTicks();
            alexPlayerModel.leftArm.xRot += (float) Math.toRadians(-55F + aimTicks * -30F);
            alexPlayerModel.leftArm.yRot += (float) Math.toRadians((-45F + aimTicks * -20F) * (-1));
            alexPlayerModel.rightArm.xRot += (float) Math.toRadians(-42F + aimTicks * -48F);
            alexPlayerModel.rightArm.yRot += (float) Math.toRadians((-15F + aimTicks * 5F) * (-1F));
        }

        alexPlayerModel.renderToBuffer(matrixStackIn, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1.0F);

    }
}
