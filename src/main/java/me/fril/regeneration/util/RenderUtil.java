package me.fril.regeneration.util;

import me.fril.regeneration.client.models.ModelArmorOverride;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class RenderUtil {
	
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
				GlStateManager.color((float) color.x, (float)color.y, (float)color.z, 1.0F / layers / 2);
				GlStateManager.depthMask(false);
			} else {
				GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
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
	
	public static void renderPlayerLaying(RenderPlayerEvent.Pre e, AbstractClientPlayer player, boolean cancelEvent) {
		GlStateManager.pushMatrix();
		ModelPlayer model = e.getRenderer().getMainModel();
		model.isChild = false;
		GlStateManager.translate(0, 0.2F, 0);
		GlStateManager.rotate(90, 1, 0, 0);
		e.setCanceled(cancelEvent);
		
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(player.getLocationSkin());
		model.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYawHead, player.cameraPitch, 0.0625f);
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
	
	public static void setupArmorModelOverride(RenderPlayer renderPlayer) {
		List<LayerRenderer<EntityLivingBase>> layers = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, renderPlayer, "field_177097_h");
		if (layers != null) {
			LayerRenderer<EntityLivingBase> armorLayer = layers.stream().filter(layer->layer instanceof LayerBipedArmor).findFirst().orElse(null);
			if (armorLayer != null) {
				Field mainModel = ReflectionHelper.findField(LayerArmorBase.class, ObfuscationReflectionHelper.remapFieldNames(LayerArmorBase.class.getName(), "field_177186_d"));
				Field legModel = ReflectionHelper.findField(LayerArmorBase.class, ObfuscationReflectionHelper.remapFieldNames(LayerArmorBase.class.getName(), "field_177189_c"));
				mainModel.setAccessible(true);
				legModel.setAccessible(true);
				try {
					ModelArmorOverride model = new ModelArmorOverride();
					mainModel.set(armorLayer, model);
					legModel.set(armorLayer, model);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
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
	}
	
	
	
	/** <a href="https://stackoverflow.com/a/41491220/10434371">Source</a> */
	public static double calculateColorBrightness(Vec3d c) {
		float r = (float) c.x, g = (float) c.y, b = (float) c.z;
		r = r <= 0.03928 ? r / 12.92F : (float)Math.pow((r + 0.055) / 1.055, 2.4);
		g = g <= 0.03928 ? g / 12.92F : (float)Math.pow((g + 0.055) / 1.055, 2.4);
		b = b <= 0.03928 ? b / 12.92F : (float)Math.pow((b + 0.055) / 1.055, 2.4);
		
		return (0.2126 * r) + (0.7152 * g) + (0.0722 * b);
	}
	
}
