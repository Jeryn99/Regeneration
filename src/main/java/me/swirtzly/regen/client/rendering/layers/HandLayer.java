package me.swirtzly.regen.client.rendering.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.client.rendering.types.RenderTypes;
import me.swirtzly.regen.common.regen.RegenCap;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3d;

import static me.swirtzly.regen.client.rendering.layers.RenderRegenLayer.renderColorCone;
import static net.minecraft.client.renderer.entity.EnderDragonRenderer.ENDERCRYSTAL_BEAM_TEXTURES;

public class HandLayer extends LayerRenderer {


    private static final RenderType field_229056_k_ = RenderType.getEntitySmoothCutout(ENDERCRYSTAL_BEAM_TEXTURES);

    public HandLayer(IEntityRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    public static void renderGlowingHands(LivingEntity livingEntity, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            if (iRegen.areHandsGlowing()) {
                Vector3d primaryColors = iRegen.getPrimaryColors();
                Vector3d secondaryColors = iRegen.getSecondaryColors();
                renderColorCone(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, livingEntity, 0.5F, 0.5F, primaryColors);
                renderColorCone(matrixStackIn, bufferIn.getBuffer(RenderTypes.REGEN_FLAMES), packedLightIn, livingEntity, 0.7F, 0.7F, secondaryColors);
            }
        });
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityModel< ? > model = getEntityModel();

   /*     BlockPos blockpos = entitylivingbaseIn.getPosition().north(8);
        if (blockpos != null) {
            float f3 = (float)blockpos.getX() + 0.5F;
            float f4 = (float)blockpos.getY() + 0.5F;
            float f5 = (float)blockpos.getZ() + 0.5F;
            float f6 = (float)((double)f3 - entitylivingbaseIn.getPosX());
            float f7 = (float)((double)f4 - entitylivingbaseIn.getPosY());
            float f8 = (float)((double)f5 - entitylivingbaseIn.getPosZ());
            matrixStackIn.translate((double)f6, (double)f7, (double)f8);
            EnderDragonRenderer.func_229059_a_(-f6, -f7, -f8, partialTicks, 0, matrixStackIn, bufferIn, packedLightIn);
        }*/

        RegenCap.get((LivingEntity) entitylivingbaseIn).ifPresent(iRegen -> {

            if (entitylivingbaseIn.isSneaking()) {
                matrixStackIn.translate(0.0F, 0.2F, 0.0F);
            }

            BipedModel< ? > bipedModel = (BipedModel) model;

            //For Regen Layers
            for (HandSide handSide : HandSide.values()) {
                matrixStackIn.push();
                bipedModel.translateHand(handSide, matrixStackIn);
                renderGlowingHands((LivingEntity) entitylivingbaseIn, matrixStackIn, bufferIn, packedLightIn);
                iRegen.getTransitionType().get().getRenderer().thirdPersonHand(handSide, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                matrixStackIn.pop();
            }
        });
    }
}
