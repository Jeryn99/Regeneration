package com.afg.regeneration.client.layers;

import com.afg.regeneration.Regeneration;
import com.afg.regeneration.client.animation.LimbRotationUtil;
import com.afg.regeneration.superpower.TimelordHandler;
import lucraft.mods.lucraftcore.superpower.SuperpowerHandler;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

/**
 * Created by AFlyingGrayson on 8/8/17
 */
@SideOnly(Side.CLIENT)
public class LayerRegenerationLimbs implements LayerRenderer<EntityPlayer> {
	private final RenderPlayer playerRenderer;
	private static final ModelPlayer playerModelLargeArms = new ModelPlayer(0.55F, false);
	private static final ModelPlayer playerModelSmallArms = new ModelPlayer(0.55F, true);

	public LayerRegenerationLimbs(RenderPlayer playerRenderer) {
		this.playerRenderer = playerRenderer;

	}

	public void doRenderLayer(EntityPlayer player, float p_177169_2_, float p_177169_3_, float p_177169_4_,
			float p_177169_5_, float p_177169_6_, float p_177169_7_, float p_177169_8_) {

		if (SuperpowerHandler.hasSuperpower(player, Regeneration.timelord)) {
			TimelordHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player,
					TimelordHandler.class);
			if (handler.regenTicks > 0) {

				Color color = new Color(255, 200, 0, 1);

				boolean smallArms = ((AbstractClientPlayer) player).getSkinType().equals("slim");
				ModelPlayer playerModel = smallArms ? playerModelSmallArms : playerModelLargeArms;

				GlStateManager.pushAttrib();
				GlStateManager.disableTexture2D();
				GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

				//Left Arm/Armwear
				LimbRotationUtil.createCustomModelRenderer(playerModel, 0, 0, -75, ModelBiped.class.getDeclaredFields()[4]);
				LimbRotationUtil.createCustomModelRenderer(playerModel, 0, 0, -75, ModelPlayer.class.getDeclaredFields()[0]);

				//Right Arm/Armwear
				LimbRotationUtil.createCustomModelRenderer(playerModel, 0, 0, 75, ModelBiped.class.getDeclaredFields()[3]);
				LimbRotationUtil.createCustomModelRenderer(playerModel, 0, 0, 75, ModelPlayer.class.getDeclaredFields()[1]);

				//Head/Headwear
				LimbRotationUtil.createCustomModelRenderer(playerModel, -20, 0, 0, ModelBiped.class.getDeclaredFields()[0]);
				LimbRotationUtil.createCustomModelRenderer(playerModel, -20, 0, 0, ModelBiped.class.getDeclaredFields()[1]);

				playerModel.setModelAttributes(this.playerRenderer.getMainModel());
				playerModel.render(player, p_177169_2_, p_177169_3_, p_177169_5_, p_177169_6_, p_177169_7_,
						p_177169_8_);

				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.color(255, 255, 255, 255);
				GlStateManager.enableTexture2D();
				GlStateManager.popAttrib();

	

				if (handler.regenTicks > 150) {
					playerModelSmallArms.bipedBody.isHidden = playerModelSmallArms.bipedLeftLeg.isHidden = playerModelSmallArms.bipedRightLeg.isHidden = playerModelSmallArms.bipedBodyWear.isHidden = playerModelSmallArms.bipedHeadwear.isHidden = playerModelSmallArms.bipedLeftLegwear.isHidden = playerModelSmallArms.bipedRightLegwear.isHidden = false;

					playerModelLargeArms.bipedBody.isHidden = playerModelLargeArms.bipedLeftLeg.isHidden = playerModelLargeArms.bipedRightLeg.isHidden = playerModelLargeArms.bipedBodyWear.isHidden = playerModelLargeArms.bipedHeadwear.isHidden = playerModelLargeArms.bipedLeftLegwear.isHidden = playerModelLargeArms.bipedRightLegwear.isHidden = false;
				} else {

					playerModelSmallArms.bipedBody.isHidden = playerModelSmallArms.bipedLeftLeg.isHidden = playerModelSmallArms.bipedRightLeg.isHidden = playerModelSmallArms.bipedBodyWear.isHidden = playerModelSmallArms.bipedHeadwear.isHidden = playerModelSmallArms.bipedLeftLegwear.isHidden = playerModelSmallArms.bipedRightLegwear.isHidden = true;

					playerModelLargeArms.bipedBody.isHidden = playerModelLargeArms.bipedLeftLeg.isHidden = playerModelLargeArms.bipedRightLeg.isHidden = playerModelLargeArms.bipedBodyWear.isHidden = playerModelLargeArms.bipedHeadwear.isHidden = playerModelLargeArms.bipedLeftLegwear.isHidden = playerModelLargeArms.bipedRightLegwear.isHidden = true;
				}
			}
		}

	}

	public boolean shouldCombineTextures() {
		return false;
	}
}
