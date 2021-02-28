package me.swirtzly.regen.client.rendering.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.client.rendering.entity.TimelordRenderer;
import me.swirtzly.regen.common.entities.TimelordEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class TimelordHeadLayer extends LayerRenderer< TimelordEntity, BipedModel< TimelordEntity > > {
    private static final PlayerModel< TimelordEntity > entitymodel = new PlayerModel<>(0F, true);

    public TimelordHeadLayer(IEntityRenderer< TimelordEntity, BipedModel< TimelordEntity > > entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, TimelordEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        entitymodel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        entitymodel.setVisible(false);
        entitymodel.bipedHead.showModel = true;
        entitymodel.bipedHeadwear.showModel = true;
        this.getEntityModel().copyModelAttributesTo(entitymodel);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(TimelordRenderer.getTimelordFace(entitylivingbaseIn)));
        entitymodel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        entitymodel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
    }
}
