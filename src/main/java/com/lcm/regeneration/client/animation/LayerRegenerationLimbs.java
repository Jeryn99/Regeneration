package com.lcm.regeneration.client.animation;

import com.lcm.regeneration.superpower.TimelordSuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by AFlyingGrayson on 8/8/17
 */
@SideOnly(Side.CLIENT)
public class LayerRegenerationLimbs implements LayerRenderer<EntityPlayer> {
	private final RenderPlayer playerRenderer;
	private static final ModelPlayer playerModelLargeArms = new ModelPlayer(0.55F, false);
	private static final ModelPlayer playerModelSmallArms = new ModelPlayer(0.55F, true);
	
	LayerRegenerationLimbs(RenderPlayer playerRenderer) { this.playerRenderer = playerRenderer; }
	
	@Override
	public void doRenderLayer(@Nonnull EntityPlayer player, float p_177169_2_, float p_177169_3_, float p_177169_4_, float p_177169_5_, float p_177169_6_, float p_177169_7_, float p_177169_8_) {
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class);
		if (handler == null || !(handler.regenTicks > 0 && handler.regenTicks < 200)) return;

		ModelPlayer playerModel = ((AbstractClientPlayer) player).getSkinType().equals("slim") ? LayerRegenerationLimbs.playerModelSmallArms : LayerRegenerationLimbs.playerModelLargeArms;

		if (handler.regenTicks > 150) {
			playerModel.bipedBody.isHidden = playerModel.bipedLeftLeg.isHidden = playerModel.bipedRightLeg.isHidden = playerModel.bipedBodyWear.isHidden = playerModel.bipedHeadwear.isHidden = playerModel.bipedLeftLegwear.isHidden = playerModel.bipedRightLegwear.isHidden = false;
		} else {
			playerModel.bipedBody.isHidden = playerModel.bipedLeftLeg.isHidden = playerModel.bipedRightLeg.isHidden = playerModel.bipedBodyWear.isHidden = playerModel.bipedHeadwear.isHidden = playerModel.bipedLeftLegwear.isHidden = playerModel.bipedRightLegwear.isHidden = true;
		}

		GlStateManager.pushAttrib();
		GlStateManager.disableTexture2D();
		GlStateManager.color(255, 200, 0,1);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

		playerModel.setModelAttributes(playerRenderer.getMainModel());
		playerModel.render(player, p_177169_2_, p_177169_3_, p_177169_5_, p_177169_6_, p_177169_7_, p_177169_8_);

		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.color(255, 255, 255, 255);
		GlStateManager.enableTexture2D();
		GlStateManager.popAttrib();
	}

	@Override public boolean shouldCombineTextures() { return false; }
}
