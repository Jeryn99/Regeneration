package com.afg.regeneration.client.animation;

import com.afg.regeneration.Regeneration;
import com.afg.regeneration.client.layers.LayerRegenerationLimbs;
import com.afg.regeneration.superpower.TimelordHandler;
import lucraft.mods.lucraftcore.superpower.SuperpowerHandler;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

/**
 * Created by AFlyingGrayson on 8/15/17
 */
@SideOnly(Side.CLIENT)
public class PlayerRenderHandler
{
	private static ArrayList<EntityPlayer> layersAddedTo = new ArrayList<>();

	@SubscribeEvent
	public void onRenderPlayerPre(RenderPlayerEvent.Pre e){

		if(SuperpowerHandler.hasSuperpower(e.getEntityPlayer(), Regeneration.timelord))
		{
			TimelordHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(e.getEntityPlayer(), TimelordHandler.class);
			if (handler.regenTicks > 0)
			{
				boolean smallArms = ((AbstractClientPlayer) e.getEntityPlayer()).getSkinType().equals("slim");
				LimbRotationUtil.createLeftArm(e.getRenderer().getMainModel(), 0, 0, -75, smallArms);
				LimbRotationUtil.createRightArm(e.getRenderer().getMainModel(), 0, 0, 75, smallArms);
				LimbRotationUtil.createHead(e.getRenderer().getMainModel(), -20, 0, 0);
			}
		}
	}

	@SubscribeEvent
	public void onRenderPlayerPost(RenderPlayerEvent.Post e){
		if(!layersAddedTo.contains(e.getEntityPlayer())){
			layersAddedTo.add(e.getEntityPlayer());
			e.getRenderer().addLayer(new LayerRegenerationLimbs(e.getRenderer()));
		}

		if(SuperpowerHandler.hasSuperpower(e.getEntityPlayer(), Regeneration.timelord))
		{
			TimelordHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(e.getEntityPlayer(), TimelordHandler.class);
			if (handler.regenTicks > 0)
			{
				boolean smallArms = ((AbstractClientPlayer) e.getEntityPlayer()).getSkinType().equals("slim");
				LimbRotationUtil.resetLeftArm(e.getRenderer().getMainModel(), smallArms);
				LimbRotationUtil.resetRightArm(e.getRenderer().getMainModel(), smallArms);
				LimbRotationUtil.resetHead(e.getRenderer().getMainModel());
			}
		}
	}

	//	@SubscribeEvent
	//	public static void onRotateArms(RenderModelEvent.SetRotationAngels e){
	//		if(e.getEntity() instanceof EntityPlayer)
	//		{
	//			e.model.bipedLeftArm.rotateAngleZ = 90;
	//			e.model.bipedRightArm.rotateAngleZ = 90;
	//		}
	//	}
}
