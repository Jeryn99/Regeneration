package com.lcm.regeneration.superpower;

import java.awt.Color;

import com.lcm.regeneration.client.animation.LimbManipulationUtil;

import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.render.SuperpowerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Created by AFlyingGrayson on 8/7/17 */
@Mod.EventBusSubscriber
public class TimelordRenderHandler implements SuperpowerRenderer.ISuperpowerRenderer {
	
	private static final ModelPlayer playerModelLargeArms = new ModelPlayer(0.55F, false);
	private static final ModelPlayer playerModelSmallArms = new ModelPlayer(0.55F, true);
	
	static {
		LimbManipulationUtil.getLimbManipulator(playerModelLargeArms, LimbManipulationUtil.Limb.LEFT_ARM).setAngles(0, 0, -75);
		LimbManipulationUtil.getLimbManipulator(playerModelLargeArms, LimbManipulationUtil.Limb.RIGHT_ARM).setAngles(0, 0, 75);
		LimbManipulationUtil.getLimbManipulator(playerModelLargeArms, LimbManipulationUtil.Limb.HEAD).setAngles(-20, 0, 0);
		LimbManipulationUtil.getLimbManipulator(playerModelSmallArms, LimbManipulationUtil.Limb.LEFT_ARM).setAngles(0, 0, -75);
		LimbManipulationUtil.getLimbManipulator(playerModelSmallArms, LimbManipulationUtil.Limb.RIGHT_ARM).setAngles(0, 0, 75);
		LimbManipulationUtil.getLimbManipulator(playerModelSmallArms, LimbManipulationUtil.Limb.HEAD).setAngles(-20, 0, 0);
	}
	
	@Override
	public void onRenderPlayer(RenderLivingBase<?> renderLivingBase, Minecraft minecraft, EntityPlayer entityPlayer, Superpower superpower, SuperpowerPlayerHandler superpowerPlayerHandler, float v, float v1, float v2, float v3, float v4, float v5, float v6) {
		
		TimelordSuperpowerHandler handler = (TimelordSuperpowerHandler) superpowerPlayerHandler;
		
		if (!(handler.regenTicks > 0 && handler.regenTicks < 200)) return;
		
		ModelBiped model = (ModelBiped) renderLivingBase.getMainModel();
		
		// State manager changes
		GlStateManager.pushAttrib();
		GlStateManager.disableTexture2D();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.depthMask(false);
		
		NBTTagCompound style = handler.getStyleNBTTag();
		Color primaryColor = new Color(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
		Color secondaryColor = new Color(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));
		
		float primaryScale = handler.regenTicks / 40f;
		float secondaryScale = handler.regenTicks / 70f;
		// Render right cone
		GlStateManager.pushMatrix();
		model.postRenderArm(0.0625F, EnumHandSide.RIGHT);
		GlStateManager.translate(0f, -0.2f, 0f);
		renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
		renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
		GlStateManager.popMatrix();
		
		// Render left cone
		GlStateManager.pushMatrix();
		model.postRenderArm(0.0625F, EnumHandSide.LEFT);
		GlStateManager.translate(0f, -0.2f, 0f);
		renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
		renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
		GlStateManager.popMatrix();
		
		// Render head cone
		GlStateManager.pushMatrix();
		GlStateManager.translate(0f, 0.3f, 0f);
		GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);
		renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
		renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
		GlStateManager.popMatrix();
		
		// Check which slightly larger model to use
		ModelPlayer playerModel = ((AbstractClientPlayer) entityPlayer).getSkinType().equals("slim") ? playerModelSmallArms : playerModelLargeArms;
		
		// Define which parts are glowing
		playerModel.bipedBody.isHidden = playerModel.bipedLeftLeg.isHidden = playerModel.bipedRightLeg.isHidden = playerModel.bipedBodyWear.isHidden = playerModel.bipedHeadwear.isHidden = playerModel.bipedLeftLegwear.isHidden = playerModel.bipedRightLegwear.isHidden = handler.regenTicks < 150;
		
		// Copy model attributes from the real player model
		playerModel.setModelAttributes(model);
		
		// Render glowing overlay
		GlStateManager.color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 1);
		playerModel.render(entityPlayer, v, v1, v3, v4, v5, v6);
		
		// Undo state manager changes
		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.color(255, 255, 255, 255);
		GlStateManager.enableTexture2D();
		GlStateManager.popAttrib();
	}
	
	private void renderCone(EntityPlayer entityPlayer, float scale, float scale2, Color color) {
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
	
	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre e) {
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(e.getEntityPlayer(), TimelordSuperpowerHandler.class);
		if (handler != null && (handler.regenTicks > 0 && handler.regenTicks < 200)) {
			LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.LEFT_ARM).setAngles(0, 0, -75);
			LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.RIGHT_ARM).setAngles(0, 0, 75);
			LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.HEAD).setAngles(-20, 0, 0);
		}
	}
}
