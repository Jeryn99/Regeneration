package me.swirtzly.regen.client.rendering.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.client.rendering.types.RenderTypes;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.util.RConstants;
import me.swirtzly.regen.util.RenderHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

import static me.swirtzly.regen.client.rendering.layers.RenderRegenLayer.renderCone;

public class HandGlowLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    public HandGlowLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderPlayer) {
        super(renderPlayer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        EntityModel model = getEntityModel();

        if (entitylivingbaseIn.isSneaking()) {
            matrixStackIn.translate(0.0F, 0.2F, 0.0F);
        }

        if (model instanceof BipedModel) {
            BipedModel bipedModel = (BipedModel) model;
            RegenCap.get(entitylivingbaseIn).ifPresent(iRegen -> {
                for (HandSide handSide : HandSide.values()) {
                    matrixStackIn.push();
                    bipedModel.translateHand(handSide, matrixStackIn);
                    iRegen.getTransitionType().create().getRenderer().layer(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    matrixStackIn.pop();
                }
            });
        }

        //TODO renderGlowingBall(model, matrixStackIn, bufferIn, HandSide.LEFT, RegenCap.get(entitylivingbaseIn).orElse(null), 1.5F);
        //TODO renderGlowingBall(model, matrixStackIn, bufferIn, HandSide.RIGHT, RegenCap.get(entitylivingbaseIn).orElse(null), 1.5F);

    }

    public static void renderGlowingBall(BipedModel<AbstractClientPlayerEntity> bipedModel, MatrixStack matrixStack, IRenderTypeBuffer buffer, HandSide side, IRegen iRegen, float scale) {
        bipedModel.translateHand(side, matrixStack);
        Vector3d primaryColor = new Vector3d(1, 0, 0);
        Vector3d secondaryColor = new Vector3d(0, 1, 0);

        Minecraft mc = Minecraft.getInstance();
        Random rand = iRegen.getLiving().world.rand;
        float factor = 0.2F;

        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0, 0.3F, 0);
        matrixStack.rotate(Vector3f.YP.rotation(mc.player.ticksExisted + RenderHelp.renderTick / 2F));
        for (int i = 0; i < 7; i++) {
            // matrixStack.rotate((mc.player.ticksExisted + RenderHelp.renderTick) * i / 70F, 1, 1, 0);
            // drawGlowingLine(new Vector3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), new Vector3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), 0.1F, primaryColor, 0);
            //  drawGlowingLine(new Vector3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), new Vector3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), 0.1F, secondaryColor, 0);
            IVertexBuilder vertexBuilder = buffer.getBuffer(RenderTypes.REGEN_FLAMES);
            RenderHelp.drawGlowingLine(matrixStack.getLast().getMatrix(), vertexBuilder, rand.nextFloat() / 2, 0.05F, (float) primaryColor.x, (float) primaryColor.y, (float) primaryColor.z, 1F, 15728640);
            RenderHelp.drawGlowingLine(matrixStack.getLast().getMatrix(), vertexBuilder, rand.nextFloat() / 2, 0.05F, (float) secondaryColor.x, (float) secondaryColor.y, (float) secondaryColor.z, 1F, 15728640);
        }
    }
}
