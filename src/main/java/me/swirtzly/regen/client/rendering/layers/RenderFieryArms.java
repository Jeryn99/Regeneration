package me.swirtzly.regen.client.rendering.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.client.rendering.types.RenderTypes;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RenderFieryArms extends LayerRenderer {

    public RenderFieryArms(IEntityRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderCone(matrixStackIn, bufferIn, (LivingEntity) entitylivingbaseIn, 1, 1, new Vector3d(1, 0, 0));
    }


    public static void renderCone(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, LivingEntity entityPlayer, float scale, float scale2, Vector3d color) {
        Tessellator tessellator = Tessellator.getInstance();
        IVertexBuilder vertexBuffer = bufferIn.getBuffer(RenderTypes.LASER);
        matrixStack.push();
        for (int i = 0; i < 8; i++) {
            matrixStack.rotate(Vector3f.YP.rotation(entityPlayer.ticksExisted * 4 + i * 45));
            matrixStack.scale(1.0f, 1.0f, 0.65f);
            vertexBuffer.pos(0.0D, 0.0D, 0.0D).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
            vertexBuffer.pos(0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
            vertexBuffer.pos(0.0D, scale2, 1.0F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
            tessellator.draw();
        }
        matrixStack.pop();
    }
}
