package me.fril.regeneration.combat;

import me.fril.regeneration.client.layers.LayerRegeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.List;

public class EmoticonsHandler {
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void on(RenderPlayerEvent.Pre e){
		List<LayerRenderer> layers = ReflectionHelper.getPrivateValue(RenderLivingBase.class, e.getRenderer(), "layerRenderers", "field_177097_h");
		for(LayerRenderer layer : layers){
			if(layer instanceof LayerRegeneration){
				EntityPlayer player = e.getEntityPlayer();
				layer.doRenderLayer(player, player.limbSwing, player.limbSwingAmount, Minecraft.getMinecraft().getRenderPartialTicks(), player.ticksExisted, player.cameraYaw, player.cameraPitch, 1.0F);
			}
		}
	}
	
}
