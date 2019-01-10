package me.fril.regeneration.proxy;

import java.util.List;
import java.util.Map;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.client.RegenKeyBinds;
import me.fril.regeneration.client.gui.InventoryTabRegeneration;
import me.fril.regeneration.client.rendering.LayerFuzz;
import me.fril.regeneration.client.rendering.LayerItemReplace;
import me.fril.regeneration.client.rendering.LayerRegeneration;
import me.fril.regeneration.util.RenderUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

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
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		skinMap.values().forEach(renderPlayer -> {

			renderPlayer.addLayer(new LayerRegeneration(renderPlayer));
			List<LayerRenderer> layers = ReflectionHelper.getPrivateValue(RenderLivingBase.class, renderPlayer, "layerRenderers", "field_177097_h");

			layers.forEach(layerRenderer -> {
				if (layerRenderer instanceof LayerHeldItem) {
					layers.remove(renderPlayer);
				}
			});

			layers.add(new LayerItemReplace(renderPlayer));

		});

	}
	
	@Override
	public void postInit() {
		super.postInit();
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		skinMap.values().forEach(RenderUtil::setupArmorModelOverride);

	}
	
}
