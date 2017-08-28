package com.afg.regeneration.client.animation;

import com.afg.regeneration.Regeneration;
import com.afg.regeneration.client.layers.LayerRegenerationLimbs;
import com.afg.regeneration.superpower.TimelordHandler;
import lucraft.mods.lucraftcore.superpower.SuperpowerHandler;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

/**
 * Created by AFlyingGrayson on 8/15/17
 */
public class PlayerRenderHandler
{
	private ArrayList<EntityPlayer> layersAddedTo = new ArrayList<>();

	@SubscribeEvent
	public void onRenderPlayerPre(RenderPlayerEvent.Pre e)
	{

		if (SuperpowerHandler.hasSuperpower(e.getEntityPlayer(), Regeneration.timelord))
		{
			TimelordHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(e.getEntityPlayer(), TimelordHandler.class);
			if (handler.regenTicks > 0)
			{
				boolean smallArms = ((AbstractClientPlayer) e.getEntityPlayer()).getSkinType().equals("slim");
				LimbRotationUtil.createLeftArm(e.getRenderer().getMainModel(), 0, 0, -75);
				LimbRotationUtil.createRightArm(e.getRenderer().getMainModel(), 0, 0, 75);
				LimbRotationUtil.createHead(e.getRenderer().getMainModel(), -20, 0, 0);
			}
		}
	}

	@SubscribeEvent
	public void onRenderPlayerPost(RenderPlayerEvent.Post e)
	{
		if (!layersAddedTo.contains(e.getEntityPlayer()))
		{
			layersAddedTo.add(e.getEntityPlayer());
			e.getRenderer().addLayer(new LayerRegenerationLimbs(e.getRenderer()));
		}

		if (SuperpowerHandler.hasSuperpower(e.getEntityPlayer(), Regeneration.timelord))
		{
			TimelordHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(e.getEntityPlayer(), TimelordHandler.class);
			if (handler.regenTicks > 0)
				for (ModelRenderer renderer : e.getRenderer().getMainModel().boxList)
				{
					if(renderer instanceof LimbRotationUtil.CustomModelRenderer)
						((LimbRotationUtil.CustomModelRenderer) renderer).reset();
				}
		}
	}
}
