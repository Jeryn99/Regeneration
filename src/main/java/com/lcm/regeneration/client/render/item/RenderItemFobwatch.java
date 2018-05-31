package com.lcm.regeneration.client.render.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderItemFobwatch extends TileEntityItemStackRenderer {


	//ModelFobWatch model = new ModelDetector();

	@Override
	public void renderByItem(ItemStack theStack, float partialTicks) {
		GlStateManager.pushMatrix();
		//model.render(0.0625f);
		GlStateManager.popMatrix();
	}


	@SubscribeEvent
	public static void setUptheStupidModelISwearToGod(ModelBakeEvent e) {
		//RObjects.Items.chameleonArch.setTileEntityItemStackRenderer(new RenderItemFobwatch());
	}
}
