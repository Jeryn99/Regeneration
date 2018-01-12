package com.lcm.regeneration.client.animation;

import com.lcm.regeneration.superpower.TimelordSuperpower;
import com.lcm.regeneration.superpower.TimelordSuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
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
		if (!SuperpowerHandler.hasSuperpower(e.getEntityPlayer(), TimelordSuperpower.INSTANCE)) return;
		
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(e.getEntityPlayer(), TimelordSuperpowerHandler.class);
		if (handler.regenTicks > 0 && handler.regenTicks < 200) {
			ModelPlayer player = e.getRenderer().getMainModel();
			
			// Left Arm/Armwear
			LimbRotationUtil.createCustomModelRenderer(player, 0, 0, -75, ModelBiped.class.getDeclaredFields()[4]);
			LimbRotationUtil.createCustomModelRenderer(player, 0, 0, -75, ModelPlayer.class.getDeclaredFields()[0]);
			
			// Right Arm/Armwear
			LimbRotationUtil.createCustomModelRenderer(player, 0, 0, 75, ModelBiped.class.getDeclaredFields()[3]);
			LimbRotationUtil.createCustomModelRenderer(player, 0, 0, 75, ModelPlayer.class.getDeclaredFields()[1]);
			
			// Head/Headwear
			LimbRotationUtil.createCustomModelRenderer(player, -20, 0, 0, ModelBiped.class.getDeclaredFields()[0]);
			LimbRotationUtil.createCustomModelRenderer(player, -20, 0, 0, ModelBiped.class.getDeclaredFields()[1]);
		}
	}
	
	@SubscribeEvent
	public void onRenderPlayerPost(RenderPlayerEvent.Post e) {
		if (!layersAddedTo.contains(e.getEntityPlayer())) {
			layersAddedTo.add(e.getEntityPlayer());
			e.getRenderer().addLayer(new LayerRegenerationLimbs(e.getRenderer()));
		}
		
		if (SuperpowerHandler.hasSuperpower(e.getEntityPlayer(), TimelordSuperpower.INSTANCE)) {
			TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(e.getEntityPlayer(), TimelordSuperpowerHandler.class);
			if (handler.regenTicks > 0) for (ModelRenderer renderer : e.getRenderer().getMainModel().boxList) {
				if (renderer instanceof LimbRotationUtil.CustomModelRenderer) ((LimbRotationUtil.CustomModelRenderer) renderer).reset();
			}
		}
	}
}
