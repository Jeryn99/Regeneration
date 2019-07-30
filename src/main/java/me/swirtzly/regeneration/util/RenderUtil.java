package me.swirtzly.regeneration.util;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.RegenerationMod;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class RenderUtil {
	
	private static final ResourceLocation VIGNETTE_TEX_PATH = new ResourceLocation(RegenerationMod.MODID, "textures/misc/vignette.png");
	public static float renderTick = Minecraft.getInstance().getRenderPartialTicks();

	private static float lastBrightnessX = GLX.lastBrightnessX;
	private static float lastBrightnessY = GLX.lastBrightnessY;

	public static void setLightmapTextureCoords(float x, float y) {
		lastBrightnessX = GLX.lastBrightnessX;
		lastBrightnessY = GLX.lastBrightnessY;
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, x, y);
	}

	public static void restoreLightMap() {
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lastBrightnessX, lastBrightnessY);
	}

	public static void drawGlowingLine(Vec3d start, Vec3d end, float thickness, Vec3d color, float alpha) {
		if (start == null || end == null)
			return;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bb = tessellator.getBuffer();
		int smoothFactor = Minecraft.getInstance().gameSettings.ambientOcclusionStatus.func_216572_a();
		int layers = 10 + smoothFactor * 20;

		GlStateManager.pushMatrix();
		GlStateManager.disableTexture();
		start = start.scale(-1D);
		end = end.scale(-1D);
		GlStateManager.translated(-start.x, -start.y, -start.z);
		start = end.subtract(start);
		end = end.subtract(end);

		{
			double x = end.x - start.x;
			double y = end.y - start.y;
			double z = end.z - start.z;
			double diff = MathHelper.sqrt(x * x + z * z);
			float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
			float pitch = (float) -(Math.atan2(y, diff) * 180.0D / Math.PI);
			GlStateManager.rotatef(-yaw, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(pitch, 1.0F, 0.0F, 0.0F);
		}

		for (int layer = 0; layer <= layers; ++layer) {
			if (layer < layers) {
				GlStateManager.color4f((float) color.x, (float) color.y, (float) color.z, 1.0F / layers / 2);
				GlStateManager.depthMask(false);
			} else {
				GlStateManager.color4f(1.0F, 1.0F, 1.0F, alpha); // SUB does this actually do anything? We're always passing in an alpha of 0...
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

		GlStateManager.enableTexture();
		GlStateManager.popMatrix();
	}

	public static void setupRenderLightning() {
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.enableAlphaTest();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.SourceFactor.CONSTANT_ALPHA.value);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		setLightmapTextureCoords(240, 240);
	}

	public static void finishRenderLightning() {
		restoreLightMap();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.disableBlend();
		GlStateManager.disableAlphaTest();
		GlStateManager.popMatrix();
	}

	
	public static void renderVignette(Vec3d color, float a, PlayerUtil.RegenState state) {
		GlStateManager.color4f((float) color.x, (float) color.y, (float) color.z, a);
		GlStateManager.disableAlphaTest();
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		Minecraft.getInstance().getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		
		MainWindow scaledRes = Minecraft.getInstance().mainWindow;
		int z = -89; // below the HUD
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(0, scaledRes.getScaledHeight(), z).tex(0, 1).endVertex();
		bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), z).tex(1.0D, 1.0D).endVertex();
		bufferbuilder.pos(scaledRes.getScaledWidth(), 0, z).tex(1, 0).endVertex();
		bufferbuilder.pos(0, 0, z).tex(0, 0).endVertex();
		tessellator.draw();
		
		GlStateManager.depthMask(true);
		GlStateManager.enableAlphaTest();
		GlStateManager.color4f(1, 1, 1, 1);
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
	
	public static void drawModelToGui(EntityModel model, int xPos, int yPos, float scale, float rotation) {
		GlStateManager.pushMatrix();
		GlStateManager.enableDepthTest();
		GlStateManager.enableBlend();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GlStateManager.translatef(xPos, yPos, 100);
		GlStateManager.rotatef(-25, 1, 0, 0);
		GlStateManager.rotatef(rotation, 0, 1, 0);
		RenderHelper.enableGUIStandardItemLighting();

		GlStateManager.lightModel(2899, RenderHelper.setColorBuffer(0.75F, 0.75F, 0.75F, 1F));
		GlStateManager.scalef(38 * scale, 34 * scale, 38 * scale);
		GlStateManager.scalef(-1, 1, 1);
		model.render(Minecraft.getInstance().player, 0, 0, Minecraft.getInstance().player.ticksExisted, 0, 0, 0.0625f);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.disableDepthTest();
		GlStateManager.popMatrix();
	}

	public static void copyModelAngles(RendererModel src, RendererModel dest) {
		src.rotateAngleX = dest.rotateAngleX;
		src.rotateAngleY = dest.rotateAngleY;
		src.rotateAngleZ = dest.rotateAngleZ;
		src.rotationPointX = dest.rotationPointX;
		src.rotationPointY = dest.rotationPointY;
		src.rotationPointZ = dest.rotationPointZ;
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
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture();
		//I think the below is my lad
		GlStateManager.blendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color4f(red, green, blue, alpha);
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferBuilder.pos(left, bottom, 0.0D).endVertex();
		bufferBuilder.pos(right, bottom, 0.0D).endVertex();
		bufferBuilder.pos(right, top, 0.0D).endVertex();
		bufferBuilder.pos(left, top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture();
		GlStateManager.disableBlend();
	}
	
}
