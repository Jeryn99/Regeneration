package me.fril.regeneration.client.rendering;

import org.lwjgl.opengl.GL11;

import me.fril.regeneration.client.rendering.model.ModelSkeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class LayerFuzz implements LayerRenderer<EntityPlayer> {
	
	private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");
	public static ModelPlayer mainModel = new ModelPlayer(0.1F, true);
	private ModelSkeleton modelSkeleton = new ModelSkeleton();
	private RenderPlayer playerRenderer;
	
	public LayerFuzz(RenderPlayer playerRenderer) {
		this.playerRenderer = playerRenderer;
	}
	
	@Override
	public void doRenderLayer(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		/*if (!CapabilityRegeneration.getForPlayer(entitylivingbaseIn).getState() == RegenState.CORRUPT)
			return;*/
		
		mainModel = playerRenderer.getMainModel();
		
		GlStateManager.pushMatrix();
		modelSkeleton.isChild = false;
		for (ModelRenderer modelRenderer : modelSkeleton.boxList) {
			if (modelRenderer != modelSkeleton.bipedHead) {
				modelRenderer.isHidden = true;
			}
			modelSkeleton.bipedHead.isHidden = false;
		}
		modelSkeleton.isSneak = entitylivingbaseIn.isSneaking();
		modelSkeleton.isRiding = entitylivingbaseIn.isRiding();
		Minecraft.getMinecraft().renderEngine.bindTexture(SKELETON_TEXTURES);
		modelSkeleton.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		mainModel = playerRenderer.getMainModel();
		GlStateManager.pushMatrix();
		
		ResourceLocation playerTexture = playerRenderer.getEntityTexture((AbstractClientPlayer) entitylivingbaseIn);
		if (playerTexture != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(playerTexture);
		}
		
		if (entitylivingbaseIn.ticksExisted % 10 == 0) {
			mainModel.bipedHead.isHidden = entitylivingbaseIn.world.rand.nextBoolean();
		}
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(1, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GlStateManager.color(0.2f, 0.2f, 1, 0.3f);
		
		if (entitylivingbaseIn.world.rand.nextInt(3) == 1) {
			GlStateManager.translate(0, entitylivingbaseIn.world.rand.nextInt(6) / 100.0f, 0);
		}
		if (entitylivingbaseIn.world.rand.nextInt(4) == 1) {
			GlStateManager.scale(1, 1 + entitylivingbaseIn.world.rand.nextInt(10) / 100.0f, 1);
		}
		
		GlStateManager.disableBlend();
		GlStateManager.scale(1, 1, 1);
		mainModel.isChild = false;
		mainModel.isSneak = entitylivingbaseIn.isSneaking();
		mainModel.isRiding = entitylivingbaseIn.isRiding();
		mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		GlStateManager.enableCull();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableCull();
		GlStateManager.scale(1, 1, 1);
		mainModel.isChild = false;
		mainModel.isSneak = entitylivingbaseIn.isSneaking();
		mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		GlStateManager.disableCull();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
