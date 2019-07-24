package me.swirtzly.regeneration.proxy;

import me.swirtzly.regeneration.client.RegenKeyBinds;
import me.swirtzly.regeneration.client.gui.parts.InventoryTabRegeneration;
import me.swirtzly.regeneration.client.rendering.entity.RenderItemOverride;
import me.swirtzly.regeneration.client.rendering.entity.RenderLindos;
import me.swirtzly.regeneration.client.rendering.layers.LayerHands;
import me.swirtzly.regeneration.client.rendering.layers.LayerRegeneration;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.common.entity.EntityItemOverride;
import me.swirtzly.regeneration.common.entity.EntityLindos;
import me.swirtzly.regeneration.util.FileUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.Map;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(new SkinChangingHandler());
		RenderingRegistry.registerEntityRenderingHandler(EntityItemOverride.class, RenderItemOverride::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLindos.class, RenderLindos::new);
	}
	
	@Override
	public void init() {
		super.init();
		
		// Galacticraft API for TABS ======================
		if (TabRegistry.getTabList().isEmpty()) {
			MinecraftForge.EVENT_BUS.register(new TabRegistry());
			TabRegistry.registerTab(new InventoryTabVanilla());
		}
		TabRegistry.registerTab(new InventoryTabRegeneration());

	}
	
	@Override
	public void postInit() {
		super.postInit();
		RegenKeyBinds.init();
		
		// Render layers ===========================================
		Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
		for (PlayerRenderer renderPlayer : skinMap.values()) {
			renderPlayer.addLayer(new LayerRegeneration(renderPlayer)); // Add Regeneration Layer
			renderPlayer.addLayer(new LayerHands(renderPlayer));
		}
		FileUtil.doSetupOnThread();
	}
	
}
