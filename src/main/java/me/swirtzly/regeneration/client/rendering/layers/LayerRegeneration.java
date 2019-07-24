package me.swirtzly.regeneration.client.rendering.layers;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.IRegenType;
import me.swirtzly.regeneration.common.types.TypeHandler;
import me.swirtzly.regeneration.util.PlayerUtil;
import me.swirtzly.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

import static me.swirtzly.regeneration.client.rendering.types.TypeFieryRenderer.renderOverlay;
import static me.swirtzly.regeneration.util.RenderUtil.drawGlowingLine;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class LayerRegeneration implements LayerRenderer<PlayerEntity> {
	
	public static final ModelPlayer playerModelSteve = new ModelPlayer(0.1F, false);
	private static PlayerRenderer playerRenderer;
	
	public LayerRegeneration(PlayerRenderer playerRenderer) {
		LayerRegeneration.playerRenderer = playerRenderer;
	}
	
	public static void renderGlowingHands(PlayerEntity player, IRegeneration handler, float scale, HandSide side) {
		Vec3d primaryColor = handler.getPrimaryColor();
		Vec3d secondaryColor = handler.getSecondaryColor();
		
		Minecraft mc = Minecraft.getInstance();
		Random rand = player.world.rand;
		float factor = 0.2F;
		
		RenderUtil.setupRenderLightning();
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.translate(0, 0.3F, 0);
		GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
		for (int i = 0; i < 7; i++) {
			GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
			drawGlowingLine(new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), 0.1F, primaryColor, 0);
			drawGlowingLine(new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), 0.1F, secondaryColor, 0);
		}
		RenderUtil.finishRenderLightning();
	}
	
	@Override
	public void doRenderLayer(PlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		IRegenType type = TypeHandler.getTypeInstance(cap.getType());
		if (cap.getState() == PlayerUtil.RegenState.REGENERATING) {
			type.getRenderer().onRenderRegenerationLayer(type, playerRenderer, cap, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		}
		
		if (cap.getState() == PlayerUtil.RegenState.POST && player.hurtTime > 0) {
			renderOverlay(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		}
		
		if (cap.isSyncingToJar()) {
			TypeHandler.getTypeInstance(TypeHandler.RegenType.FIERY).getRenderer().onRenderRegenerationLayer(type, playerRenderer, cap, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}
	
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
}
