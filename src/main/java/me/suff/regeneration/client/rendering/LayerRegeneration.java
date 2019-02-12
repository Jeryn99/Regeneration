package me.suff.regeneration.client.rendering;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.util.RegenState;
import me.suff.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class LayerRegeneration implements LayerRenderer<EntityPlayer> {
	
	public static final ModelPlayer playerModelSteve = new ModelPlayer(0.1F, false);
	public static final ModelPlayer playerModelAlex = new ModelPlayer(0.1F, true);
	
	private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation(RegenerationMod.MODID, "textures/misc/post.png");
	
	private RenderPlayer playerRenderer;
	
	public LayerRegeneration(RenderPlayer playerRenderer) {
		this.playerRenderer = playerRenderer;
	}
	
	@Override
	public void render(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scalef) {
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		if (cap.getState() == RegenState.REGENERATING) {
			cap.getType().getRenderer().onRenderRegenerationLayer(cap.getType(), playerRenderer, cap, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scalef);
		} else if (cap.areHandsGlowing()) {
			renderGlowingHands(player, cap, scalef);
		}
		
		if (cap.getState() == RegenState.POST && player.hurtTime > 0) {
			renderPost(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scalef);
		}
	}
	
	private void renderGlowingHands(EntityPlayer player, IRegeneration handler, float scalef) {
		Vec3d primaryColor = handler.getPrimaryColor();
		Vec3d secondaryColor = handler.getSecondaryColor();
		
		Minecraft mc = Minecraft.getInstance();
		Random rand = player.world.rand;
		float f = 0.2F;
		
		for (int j = 0; j < 2; j++) {
			RenderUtil.setupRenderLightning();
			
			if (j == 0)
				playerRenderer.getMainModel().bipedRightArm.postRender(scalef);
			else
				playerRenderer.getMainModel().bipedLeftArm.postRender(scalef);
			
			GlStateManager.scalef(1.5F, 1.5F, 1.5F);
			
			if (player.isSneaking()) {
				GlStateManager.translatef(0.0F, 0.2F, 0.0F);
			}
			
			GlStateManager.translatef(0, 0.3F, 0);
			GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
			
			for (int i = 0; i < 7; i++) {
				GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
				RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, primaryColor, 0);
				RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, secondaryColor, 0);
			}
			
			RenderUtil.finishRenderLightning();
		}
	}
	
	public void renderPost(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scalef) {
		boolean flag = player.isInvisible();
		ModelBase playerModel = playerRenderer.getMainModel();
		GlStateManager.depthMask(!flag);
		this.playerRenderer.bindTexture(GLOW_TEXTURE);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		float f = (float) player.ticksExisted + partialTicks;
		GlStateManager.translatef(f * 0.01F, f * 0.01F, 0.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.enableBlend();
		float f1 = 0.5F;
		GlStateManager.color4f(0.5F, 0.5F, 0.5F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		playerModel.setModelAttributes(this.playerRenderer.getMainModel());
		Minecraft.getInstance().entityRenderer.setupFogColor(true);
		playerModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scalef);
		Minecraft.getInstance().entityRenderer.setupFogColor(false);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(flag);
	}
	
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
}
