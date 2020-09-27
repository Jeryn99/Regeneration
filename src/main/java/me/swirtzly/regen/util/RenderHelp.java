package me.swirtzly.regen.util;


import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;

public class RenderHelp {

    public static float renderTick = Minecraft.getInstance().getRenderPartialTicks();



    public static void renderFilledBox(Matrix4f matrix, IVertexBuilder builder, AxisAlignedBB boundingBox, float red, float green, float blue, float alpha, int combinedLightIn) {
        renderFilledBox(matrix, builder, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ, red, green, blue, alpha, combinedLightIn);
    }

    public static void renderFilledBox(Matrix4f matrix, IVertexBuilder builder, float startX, float startY, float startZ, float endX, float endY, float endZ, float red, float green, float blue, float alpha, int combinedLightIn) {
        //down
        builder.pos(matrix, startX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();

        //up
        builder.pos(matrix, startX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();

        //east
        builder.pos(matrix, startX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();

        //west
        builder.pos(matrix, startX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();

        //south
        builder.pos(matrix, endX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();

        //north
        builder.pos(matrix, startX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
    }

    public static void drawGlowingLine(Matrix4f matrix, IVertexBuilder builder, float length, float width, float red, float green, float blue, float alpha, int combinedLightIn) {
        AxisAlignedBB box = new AxisAlignedBB(-width / 2F, 0, -width / 2F, width / 2F, length, width / 2F);
        renderFilledBox(matrix, builder, box, 1F, 1F, 1F, alpha, combinedLightIn);

        for(int i = 0; i < 3; i++) {
            renderFilledBox(matrix, builder, box.grow(i * 0.5F * 0.0625F), red, green, blue, (1F / i / 2) * alpha, combinedLightIn);
        }
    }



}
