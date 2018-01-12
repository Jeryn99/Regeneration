package com.lcm.regeneration.superpower;

import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.render.SuperpowerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;

import java.awt.*;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
public class TimelordRenderHandler implements SuperpowerRenderer.ISuperpowerRenderer {
	@Override
	public void onRenderPlayer(RenderLivingBase<?> renderLivingBase, Minecraft minecraft, EntityPlayer entityPlayer, Superpower superpower, SuperpowerPlayerHandler superpowerPlayerHandler, float v, float v1, float v2, float v3, float v4, float v5, float v6) {

		TimelordSuperpowerHandler handler = (TimelordSuperpowerHandler) superpowerPlayerHandler;

		if (!(handler.regenTicks > 0 && handler.regenTicks < 200)) return;

		ModelBiped model = (ModelBiped) renderLivingBase.getMainModel();


		//State manager changes
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.depthMask(false);

		//Render right cone
		GlStateManager.pushMatrix();
		model.postRenderArm(0.0625F, EnumHandSide.RIGHT);
		GlStateManager.translate(0f, -0.2f, 0f);
		renderCone(entityPlayer, handler.regenTicks);
		GlStateManager.popMatrix();

		//Render left cone
		GlStateManager.pushMatrix();
		model.postRenderArm(0.0625F, EnumHandSide.LEFT);
		GlStateManager.translate(0f, -0.2f, 0f);
		renderCone(entityPlayer, handler.regenTicks);
		GlStateManager.popMatrix();

		//Render head cone
		GlStateManager.pushMatrix();
		GlStateManager.translate(0f, 0.3f, 0f);
		GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);
		renderCone(entityPlayer, handler.regenTicks);
		GlStateManager.popMatrix();

		//Undo state manager changes
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
	}

	private void renderCone(EntityPlayer entityPlayer, int regenTicks){
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		int green = 120 + entityPlayer.getRNG().nextInt(80);
		Color color = new Color(255, green, 0, 100);
		float scale = (float) regenTicks / 40;
		float f3 = (float) regenTicks / 40;
		for (int i = 0; i < 8; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(1.0f, 1.0f, 0.65f);
			vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			vertexBuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexBuffer.pos(-0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexBuffer.pos(0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexBuffer.pos(0.0D, scale, 1.0F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			vertexBuffer.pos(-0.266D * f3, scale, -0.5F * f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
	}
}
