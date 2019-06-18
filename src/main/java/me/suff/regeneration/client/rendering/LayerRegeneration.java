package me.suff.regeneration.client.rendering;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.types.IRegenType;
import me.suff.regeneration.common.types.TypeHandler;
import me.suff.regeneration.util.RegenState;
import me.suff.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

import static me.suff.regeneration.client.rendering.TypeFieryRenderer.renderOverlay;
import static me.suff.regeneration.util.RenderUtil.drawGlowingLine;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class LayerRegeneration implements LayerRenderer<EntityPlayer> {
	
	public static final ModelPlayer playerModelSteve = new ModelPlayer(0.1F, false);
	private static RenderPlayer playerRenderer;
	
	public LayerRegeneration(RenderPlayer playerRenderer) {
		LayerRegeneration.playerRenderer = playerRenderer;
	}

	public static void renderGlowingHands(EntityPlayer player, IRegeneration handler, float scale, EnumHandSide side) {
		Vec3d primaryColor = handler.getPrimaryColor();
		Vec3d secondaryColor = handler.getSecondaryColor();

		Minecraft mc = Minecraft.getMinecraft();
		Random rand = player.world.rand;
		float f = 0.2F;

		RenderUtil.setupRenderLightning();
        GlStateManager.scale(scale, scale, scale);
			GlStateManager.translate(0, 0.3F, 0);
			GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
			for (int i = 0; i < 7; i++) {
				GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
				drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, primaryColor, 0);
				drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, secondaryColor, 0);
			}
		RenderUtil.finishRenderLightning();
	}

	@Override
	public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		if (cap.getState() == RegenState.REGENERATING) {
			IRegenType type = TypeHandler.getTypeInstance(cap.getType());
			type.getRenderer().onRenderRegenerationLayer(type, playerRenderer, cap, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		}

		if (cap.getState() == RegenState.POST && player.hurtTime > 0) {
			renderOverlay(playerRenderer, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}


	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
}
