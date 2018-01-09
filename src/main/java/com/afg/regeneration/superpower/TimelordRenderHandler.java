package com.afg.regeneration.superpower;

import java.awt.Color;
import java.util.Random;

import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.render.SuperpowerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
public class TimelordRenderHandler implements SuperpowerRenderer.ISuperpowerRenderer {
	@Override
	public void onRenderPlayer(RenderLivingBase<?> renderLivingBase, Minecraft minecraft, EntityPlayer entityPlayer, Superpower superpower, SuperpowerPlayerHandler superpowerPlayerHandler, float v, float v1, float v2, float v3, float v4, float v5, float v6) {
		TimelordSuperpowerHandler handler = (TimelordSuperpowerHandler) superpowerPlayerHandler;
		if (!(handler.regenTicks > 0 && handler.regenTicks < 200)) return;
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		Random random = entityPlayer.getRNG();
		
		GlStateManager.enableCull();
		GlStateManager.disableCull();
		GlStateManager.shadeModel(7425);
		GlStateManager.disableTexture2D();
		GlStateManager.enableAlpha();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		
		float scale;
		ModelBase var10000 = renderLivingBase.getMainModel();
		ModelBiped model = (ModelBiped) var10000;
		
		for (int i = 0; i < 8; i++) {
			GlStateManager.pushMatrix();
			model.postRenderArm(0.0625F, EnumHandSide.RIGHT);
			GlStateManager.translate(0f, -0.2f, 0f);
			
			int green = 120 + random.nextInt(80);
			
			GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(1.0f, 1.0f, 0.65f);
			Color color = new Color(255, green, 0, 100);
			scale = (float) handler.regenTicks / 40;
			float f3 = (float) handler.regenTicks / 40;
			vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(-0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(0.0D, scale, 1.0F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(-0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();
			
			GlStateManager.popMatrix();
		}
		
		for (int i = 0; i < 8; i++) {
			GlStateManager.pushMatrix();
			model.postRenderArm(0.0625F, EnumHandSide.LEFT);
			GlStateManager.translate(0f, -0.2f, 0f);
			
			int green = 120 + random.nextInt(80);
			
			GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(1.0f, 1.0f, 0.65f);
			Color color = new Color(255, green, 0, 100);
			scale = (float) handler.regenTicks / 40;
			float f3 = (float) handler.regenTicks / 40;
			vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(-0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(0.0D, scale, 1.0F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(-0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();
			
			GlStateManager.popMatrix();
		}
		
		for (int i = 0; i < 8; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0f, 0.3f, 0f);
			GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);
			
			int green = 120 + random.nextInt(80);
			
			GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(1.0f, 1.0f, 0.65f);
			Color color = new Color(255, green, 0, 100);
			scale = (float) handler.regenTicks / 40;
			float f3 = (float) handler.regenTicks / 40;
			vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(-0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(0.0D, scale, 1.0F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexbuffer.pos(-0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();
			
			GlStateManager.popMatrix();
		}
		
		GlStateManager.disableCull();
		GlStateManager.shadeModel(7424);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
	}
	
	@Override public void applyColor() {}
	@Override public void onRenderHandEvent(RenderHandEvent e) {}
	@Override public void onRenderSpecificHandEvent(RenderSpecificHandEvent e) {}
	@Override public void onRenderFirstPersonArmRPAPI(EnumHandSide side) {}
}
