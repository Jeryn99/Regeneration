package me.fril.regeneration.proxy;

import java.util.Map;

import me.fril.regeneration.client.RegenKeyBinds;
import me.fril.regeneration.client.gui.InventoryTabRegeneration;
import me.fril.regeneration.client.layers.LayerRegeneration;
import me.fril.regeneration.util.RenderUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class ClientProxy extends CommonProxy {
	
	@Override
	public void init() {
		super.init();
		
		// Registering the mods Keybinds
		RegenKeyBinds.init();
		
		// Galacticraft API for TABS
		if (TabRegistry.getTabList().isEmpty()) {
			MinecraftForge.EVENT_BUS.register(new TabRegistry());
			TabRegistry.registerTab(new InventoryTabVanilla());
		}
		TabRegistry.registerTab(new InventoryTabRegeneration());
		
		// Adding Render Layers
		for (RenderPlayer playerRender : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
			playerRender.addLayer(new LayerRegeneration(playerRender));
		}
	}
	
	@Override
	public void postInit() {
		super.postInit();
		
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		if (skinMap != null) {
			RenderPlayer defaultRender = skinMap.get("default");
			RenderUtil.setupArmorModelOverride(defaultRender);
			
			RenderPlayer slimRender = skinMap.get("slim");
			RenderUtil.setupArmorModelOverride(slimRender);
		}
	}
	
}
