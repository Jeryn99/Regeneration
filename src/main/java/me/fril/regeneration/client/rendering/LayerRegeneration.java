package me.fril.regeneration.client.rendering;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.RegenState;
import me.fril.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class LayerRegeneration implements LayerRenderer<EntityPlayer> {
	
	public static final ModelPlayer playerModelSteve = new ModelPlayer(0.1F, false);
	public static final ModelPlayer playerModelAlex = new ModelPlayer(0.1F, true);
	
	private RenderPlayer playerRenderer;
	
	public LayerRegeneration(RenderPlayer playerRenderer) {
		this.playerRenderer = playerRenderer;
	}
	
	@Override
	public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		if (cap.getState() == RegenState.REGENERATING) {
			cap.getType().getRenderer().onRenderRegenerationLayer(cap.getType(), playerRenderer, cap, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		} else if (cap.areHandsGlowing())
			renderGlowingHands(player, cap, scale);
	}
	
	private void renderGlowingHands(EntityPlayer player, IRegeneration handler, float scale) {
		Vec3d primaryColor = handler.getPrimaryColor();
		Vec3d secondaryColor = handler.getSecondaryColor();
		
		Minecraft mc = Minecraft.getMinecraft();
		Random rand = player.world.rand;
		float f = 0.2F;
		
		for (int j = 0; j < 2; j++) {
			RenderUtil.setupRenderLightning();
			
			if (j == 0)
				playerRenderer.getMainModel().bipedRightArm.postRender(scale);
			else
				playerRenderer.getMainModel().bipedLeftArm.postRender(scale);
			
			GlStateManager.scale(1.5F, 1.5F, 1.5F);
			
			if (player.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}
			
			GlStateManager.translate(0, 0.3F, 0);
			GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
			
			for (int i = 0; i < 7; i++) {
				GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
				RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, primaryColor, 0);
				RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, secondaryColor, 0);
			}
			
			RenderUtil.finishRenderLightning();
		}
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
}
