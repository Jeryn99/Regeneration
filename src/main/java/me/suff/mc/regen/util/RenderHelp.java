package me.suff.mc.regen.util;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RenderHelp {

    private static final ResourceLocation VIG = new ResourceLocation("regen:textures/vignette.png");
    private static int screenWidth, screenHeight;

    public static void renderFilledBox(Matrix4f matrix, VertexConsumer builder, AABB boundingBox, float red, float green, float blue, float alpha, int combinedLightIn) {
        renderFilledBox(matrix, builder, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ, red, green, blue, alpha, combinedLightIn);
    }

    public static void renderFilledBox(Matrix4f matrix, VertexConsumer builder, float startX, float startY, float startZ, float endX, float endY, float endZ, float red, float green, float blue, float alpha, int combinedLightIn) {
        //down
        builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();

        //up
        builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();

        //east
        builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();

        //west
        builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();

        //south
        builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();

        //north
        builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
        builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).uv2(combinedLightIn).endVertex();
    }

    public static void drawGlowingLine(Matrix4f matrix, VertexConsumer builder, float length, float width, float red, float green, float blue, float alpha, int combinedLightIn) {
        AABB box = new AABB(-width / 2F, 0, -width / 2F, width / 2F, length, width / 2F);
        renderFilledBox(matrix, builder, box, 1F, 1F, 1F, alpha, combinedLightIn);

        for (int i = 0; i < 3; i++) {
            renderFilledBox(matrix, builder, box.inflate(i * 0.5F * 0.0625F), red, green, blue, (1F / i / 2) * alpha, combinedLightIn);
        }
    }

    public static void renderVig(Vec3 vector3d, float alpha) {
        screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            float f1 =0.2F;//TODO Look back at GUI
            f1 = Mth.clamp(f1, 0.0F, 1.0F);
            RenderSystem.setShaderColor(f1, f1, f1, 1.0F);

        RenderSystem.setShaderColor((float) vector3d.x, (float) vector3d.y, (float) vector3d.z, alpha);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VIG);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(0.0D, screenHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(screenWidth, screenHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(screenWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }


    public static void drawRect(int left, int top, int right, int bottom, float red, float green, float blue, float alpha) {
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.setShaderColor(red, green, blue, alpha);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        bufferBuilder.vertex(left, bottom, 0.0D).endVertex();
        bufferBuilder.vertex(right, bottom, 0.0D).endVertex();
        bufferBuilder.vertex(right, top, 0.0D).endVertex();
        bufferBuilder.vertex(left, top, 0.0D).endVertex();
        tessellator.end();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

}
