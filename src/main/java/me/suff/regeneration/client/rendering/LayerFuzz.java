package me.suff.regeneration.client.rendering;

import me.suff.regeneration.client.rendering.model.ModelSkeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ModelPlayer;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LayerFuzz implements LayerRenderer<EntityPlayer> {
	
	private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");
	public static ModelPlayer mainModel = new ModelPlayer(0.1F, true);
	private ModelSkeleton modelSkeleton = new ModelSkeleton();
	private RenderPlayer playerRenderer;
	
	public LayerFuzz(RenderPlayer playerRenderer) {
		this.playerRenderer = playerRenderer;
	}
	
	@SuppressWarnings("unused")
	@Override
	public void render(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scalef) {
		/*if (!CapabilityRegeneration.getForPlayer(player).getState() == RegenState.CORRUPT)
			return;*/
		if (true)
			return;
		
		mainModel = playerRenderer.getMainModel();
		
		GlStateManager.pushMatrix();
		modelSkeleton.isChild = false;
		for (ModelRenderer modelRenderer : modelSkeleton.boxList) {
			if (modelRenderer != modelSkeleton.bipedHead) {
				modelRenderer.isHidden = true;
			}
			modelSkeleton.bipedHead.isHidden = false;
		}
		modelSkeleton.isSneak = player.isSneaking();
		modelSkeleton.isRiding = player.isRiding();
		Minecraft.getInstance().getTextureManager().bindTexture(SKELETON_TEXTURES);
		modelSkeleton.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scalef);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		mainModel = playerRenderer.getMainModel();
		GlStateManager.pushMatrix();
		
		ResourceLocation playerTexture = playerRenderer.getEntityTexture((AbstractClientPlayer) player);
		if (playerTexture != null) {
			Minecraft.getInstance().getTextureManager().bindTexture(playerTexture);
		}
		
		if (player.ticksExisted % 10 == 0) {
			mainModel.bipedHead.isHidden = player.world.rand.nextBoolean();
		}
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(1, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GlStateManager.color4f(0.2f, 0.2f, 1, 0.3f);
		
		if (player.world.rand.nextInt(3) == 1) {
			GlStateManager.translatef(0, player.world.rand.nextInt(6) / 100.0f, 0);
		}
		if (player.world.rand.nextInt(4) == 1) {
			GlStateManager.scalef(1, 1 + player.world.rand.nextInt(10) / 100.0f, 1);
		}
		
		GlStateManager.disableBlend();
		GlStateManager.scalef(1, 1, 1);
		mainModel.isChild = false;
		mainModel.isSneak = player.isSneaking();
		mainModel.isRiding = player.isRiding();
		mainModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scalef);
		
		GlStateManager.enableCull();
		GlStateManager.color4f(1, 1, 1, 1);
		GlStateManager.disableCull();
		GlStateManager.scalef(1, 1, 1);
		mainModel.isChild = false;
		mainModel.isSneak = player.isSneaking();
		mainModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scalef);
		
		GlStateManager.disableCull();
		GlStateManager.color4f(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
