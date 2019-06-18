package me.suff.regeneration.proxy;

import me.suff.regeneration.client.RegenKeyBinds;
import me.suff.regeneration.client.gui.InventoryTabRegeneration;
import me.suff.regeneration.client.rendering.LayerHands;
import me.suff.regeneration.client.rendering.LayerRegeneration;
import me.suff.regeneration.client.rendering.entity.RenderItemOverride;
import me.suff.regeneration.client.rendering.entity.RenderLindos;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.common.entity.EntityItemOverride;
import me.suff.regeneration.common.entity.EntityLindos;
import me.suff.regeneration.compat.lucraft.LucraftCoreHandler;
import me.suff.regeneration.util.EnumCompatModids;
import me.suff.regeneration.util.FileUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
		
		// LC Core
		if (EnumCompatModids.LCCORE.isLoaded()) {
			LucraftCoreHandler.registerEntry();
		}
	}
	
	@Override
	public void postInit() {
		super.postInit();
		RegenKeyBinds.init();
		
		// Render layers ===========================================
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		for (RenderPlayer renderPlayer : skinMap.values()) {
			renderPlayer.addLayer(new LayerRegeneration(renderPlayer)); // Add Regeneration Layer
			renderPlayer.addLayer(new LayerHands(renderPlayer));
		}
		FileUtil.doSetupOnThread();
	}
	
}
