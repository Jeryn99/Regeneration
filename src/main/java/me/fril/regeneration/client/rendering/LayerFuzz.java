package me.fril.regeneration.client.rendering;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

//Bored this is just for me messing around
public class LayerFuzz implements LayerRenderer<EntityPlayer> {
	
	public static ModelPlayer mainModel = new ModelPlayer(0.1F, true);
	private RenderPlayer playerRenderer;
	public LayerFuzz(RenderPlayer playerRenderer) {
		this.playerRenderer = playerRenderer;
	}
	
	@Override
	public void doRenderLayer(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		mainModel = playerRenderer.getMainModel();
		GlStateManager.pushMatrix();
		
		ResourceLocation playerTexture = playerRenderer.getEntityTexture((AbstractClientPlayer) entitylivingbaseIn);
		if (playerTexture != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(playerTexture);
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(1, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GlStateManager.color(0.2f, 0.2f, 1, 0.5f);
		
		if (entitylivingbaseIn.world.rand.nextInt(3) == 1) {
			GlStateManager.translate(0, entitylivingbaseIn.world.rand.nextInt(6) / 100.0f, 0);
		}
		if (entitylivingbaseIn.world.rand.nextInt(4) == 1) {
			GlStateManager.scale(1, 1 + entitylivingbaseIn.world.rand.nextInt(10) / 100.0f, 1);
		}
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GlStateManager.scale(1,1,1);
		mainModel.isChild = false;
		mainModel.isSneak = entitylivingbaseIn.isSneaking();
		mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GlStateManager.color(1, 1, 1, 1);
		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.popMatrix();
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
