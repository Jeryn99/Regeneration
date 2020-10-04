package me.swirtzly.regen.client.rendering.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.common.entities.OverrideEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class ItemOverrideRenderer extends EntityRenderer<OverrideEntity> {

    public ItemOverrideRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(OverrideEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.getItem().isEmpty()) return;
        matrixStackIn.push();
        matrixStackIn.translate(0, 0.17F, 0);
        matrixStackIn.rotate(Vector3f.YP.rotation(-entityIn.rotationYaw));
        IBakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(entityIn.getItem(), entityIn.world, Minecraft.getInstance().player);
        Minecraft.getInstance().getItemRenderer().renderItem(entityIn.getItem(), ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, ibakedmodel);
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(OverrideEntity entity) {
        return null;
    }
}
