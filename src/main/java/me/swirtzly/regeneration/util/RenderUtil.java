package me.swirtzly.regeneration.util;

import me.swirtzly.regeneration.RegenerationMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.ModelLoader;
import org.lwjgl.opengl.GL11;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class RenderUtil {

    private static final ResourceLocation VIGNETTE_TEX_PATH = new ResourceLocation(RegenerationMod.MODID, "textures/misc/vignette.png");
    public static float renderTick = Minecraft.getMinecraft().getRenderPartialTicks();
    private static float lastBrightnessX = OpenGlHelper.lastBrightnessX;
    private static float lastBrightnessY = OpenGlHelper.lastBrightnessY;

    public static void setLightmapTextureCoords(float x, float y) {
        lastBrightnessX = OpenGlHelper.lastBrightnessX;
        lastBrightnessY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, x, y);
    }

    public static void restoreLightMap() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
    }

    public static void setItemRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    public static void drawGlowingLine(Vec3d start, Vec3d end, float thickness, Vec3d color, float alpha) {
        if (start == null || end == null)
            return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bb = tessellator.getBuffer();
        int smoothFactor = Minecraft.getMinecraft().gameSettings.ambientOcclusion;
        int layers = 10 + smoothFactor * 20;

        GlStateManager.pushMatrix();
        start = start.scale(-1D);
        end = end.scale(-1D);
        GlStateManager.translate(-start.x, -start.y, -start.z);
        start = end.subtract(start);
        end = end.subtract(end);

        {
            double x = end.x - start.x;
            double y = end.y - start.y;
            double z = end.z - start.z;
            double diff = MathHelper.sqrt(x * x + z * z);
            float yaw = (float) (Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F;
            float pitch = (float) -(Math.atan2(y, diff) * 180.0D / 3.141592653589793D);
            GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        }

        for (int layer = 0; layer <= layers; ++layer) {
            if (layer < layers) {
                GlStateManager.color((float) color.x, (float) color.y, (float) color.z, 1.0F / layers / 2);
                GlStateManager.depthMask(false);
            } else {
                GlStateManager.color(1.0F, 1.0F, 1.0F, alpha); // SUB does this actually do anything? We're always passing in an alpha of 0...
                GlStateManager.depthMask(true);
            }
            double size = thickness + (layer < layers ? layer * (1.25D / layers) : 0.0D);
            double d = (layer < layers ? 1.0D - layer * (1.0D / layers) : 0.0D) * 0.1D;
            double width = 0.0625D * size;
            double height = 0.0625D * size;
            double length = start.distanceTo(end) + d;

            bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            bb.pos(-width, height, length).endVertex();
            bb.pos(width, height, length).endVertex();
            bb.pos(width, height, -d).endVertex();
            bb.pos(-width, height, -d).endVertex();
            bb.pos(width, -height, -d).endVertex();
            bb.pos(width, -height, length).endVertex();
            bb.pos(-width, -height, length).endVertex();
            bb.pos(-width, -height, -d).endVertex();
            bb.pos(-width, -height, -d).endVertex();
            bb.pos(-width, -height, length).endVertex();
            bb.pos(-width, height, length).endVertex();
            bb.pos(-width, height, -d).endVertex();
            bb.pos(width, height, length).endVertex();
            bb.pos(width, -height, length).endVertex();
            bb.pos(width, -height, -d).endVertex();
            bb.pos(width, height, -d).endVertex();
            bb.pos(width, -height, length).endVertex();
            bb.pos(width, height, length).endVertex();
            bb.pos(-width, height, length).endVertex();
            bb.pos(-width, -height, length).endVertex();
            bb.pos(width, -height, -d).endVertex();
            bb.pos(width, height, -d).endVertex();
            bb.pos(-width, height, -d).endVertex();
            bb.pos(-width, -height, -d).endVertex();
            tessellator.draw();
        }

        GlStateManager.popMatrix();
    }

    public static void setupRenderLightning() {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        setLightmapTextureCoords(240, 240);
    }

    public static void finishRenderLightning() {
        restoreLightMap();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
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

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
    }

    public static void renderVignette(Vec3d color, float a, PlayerUtil.RegenState state) {
        GlStateManager.color((float) color.x, (float) color.y, (float) color.z, a);
        GlStateManager.disableAlpha();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Minecraft.getMinecraft().getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int z = -89; // below the HUD
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0, scaledRes.getScaledHeight(), z).tex(0, 1).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), z).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), 0, z).tex(1, 0).endVertex();
        bufferbuilder.pos(0, 0, z).tex(0, 0).endVertex();
        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableAlpha();
        GlStateManager.color(1, 1, 1, 1);
    }

    /**
     * <a href="https://stackoverflow.com/a/41491220/10434371">Source</a>
     */
    public static double calculateColorBrightness(Vec3d c) {
        float r = (float) c.x, g = (float) c.y, b = (float) c.z;
        r = r <= 0.03928 ? r / 12.92F : (float) Math.pow((r + 0.055) / 1.055, 2.4);
        g = g <= 0.03928 ? g / 12.92F : (float) Math.pow((g + 0.055) / 1.055, 2.4);
        b = b <= 0.03928 ? b / 12.92F : (float) Math.pow((b + 0.055) / 1.055, 2.4);

        return (0.2126 * r) + (0.7152 * g) + (0.0722 * b);
    }

    public static void drawModelToGui(ModelBase model, int xPos, int yPos, float scale, float rotation) {
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.translate(xPos, yPos, 100);
        GlStateManager.rotate(-25, 1, 0, 0);
        GlStateManager.rotate(rotation, 0, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();

        GlStateManager.glLightModel(2899, RenderHelper.setColorBuffer(0.75F, 0.75F, 0.75F, 1F));
        GlStateManager.scale(38 * scale, 34 * scale, 38 * scale);
        GlStateManager.scale(-1, 1, 1);
        model.render(Minecraft.getMinecraft().player, 0, 0, Minecraft.getMinecraft().player.ticksExisted, 0, 0, 0.0625f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

}
