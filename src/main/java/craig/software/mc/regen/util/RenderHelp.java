package craig.software.mc.regen.util;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RenderHelp {

    private static final ResourceLocation VIG = new ResourceLocation("regen:textures/vignette.png");
    private static final ResourceLocation VIGNETTE_LOCATION = new ResourceLocation("textures/misc/vignette.png");

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

    public static void renderVig(Vec3 vector3d, float alpha) {

    }

}
