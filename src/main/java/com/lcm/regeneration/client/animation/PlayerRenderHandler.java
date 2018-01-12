package com.lcm.regeneration.client.animation;

import com.lcm.regeneration.superpower.TimelordSuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

/**
 * Created by AFlyingGrayson on 8/15/17
 */
public class PlayerRenderHandler {
	private ArrayList<EntityPlayer> layersAddedTo = new ArrayList<>();
	
	@SubscribeEvent
	public void onRenderPlayerPre(RenderPlayerEvent.Pre e) {
		if (!layersAddedTo.contains(e.getEntityPlayer())) {
			layersAddedTo.add(e.getEntityPlayer());
			e.getRenderer().addLayer(new LayerRegenerationLimbs(e.getRenderer()));
		}

		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(e.getEntityPlayer(), TimelordSuperpowerHandler.class);
		if (handler == null || handler.regenTicks > 0 && handler.regenTicks < 200) {
			LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.LEFT_ARM).setAngles(0, 0, -75);
			LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.RIGHT_ARM).setAngles(0, 0, 75);
			LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.HEAD).setAngles(-20, 0, 0);
		}
	}
}
