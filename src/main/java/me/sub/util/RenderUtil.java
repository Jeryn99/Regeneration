package me.sub.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class RenderUtil {

    private static float lastBrightnessX = OpenGlHelper.lastBrightnessX;
    private static float lastBrightnessY = OpenGlHelper.lastBrightnessY;

    //Render a cones
    public static void renderCone(EntityPlayer entityPlayer, float scale, float scale2, Color color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        for (int i = 0; i < 8; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(1.0f, 1.0f, 0.65f);
            vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
            vertexBuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(0.0D, scale2, 1.0F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }

    public static void setLightmapTextureCoords(float x, float y) {
        lastBrightnessX = OpenGlHelper.lastBrightnessX;
        lastBrightnessY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, x, y);
    }

    public static void restoreLightMap() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
    }

}
