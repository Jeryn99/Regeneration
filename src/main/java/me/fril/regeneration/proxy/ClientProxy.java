package me.fril.regeneration.proxy;

import java.util.Map;

import me.fril.regeneration.client.RegenKeyBinds;
import me.fril.regeneration.client.gui.InventoryTabRegeneration;
import me.fril.regeneration.client.rendering.LayerFuzz;
import me.fril.regeneration.client.rendering.LayerItemReplace;
import me.fril.regeneration.client.rendering.LayerRegeneration;
import me.fril.regeneration.common.EntityFobWatch;
import me.fril.regeneration.compat.lucraft.LucraftCoreHandler;
import me.fril.regeneration.util.EnumCompatModids;
import me.fril.regeneration.util.RenderUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		RenderingRegistry.registerEntityRenderingHandler(EntityFobWatch.class, (RenderManager renderManagerIn) -> new RenderEntityItem(renderManagerIn, Minecraft.getMinecraft().getRenderItem()));
	}
	
	@Override
	public void init() {
		super.init();
		
		// Registering the mods Keybinds
		RegenKeyBinds.init();
		
		// Galacticraft API for TABS ======================
		if (TabRegistry.getTabList().isEmpty()) {
			MinecraftForge.EVENT_BUS.register(new TabRegistry());
			TabRegistry.registerTab(new InventoryTabVanilla());
		}
		TabRegistry.registerTab(new InventoryTabRegeneration());
		
		// LC Core
		if (Loader.isModLoaded(EnumCompatModids.LCCORE.getModid())) {
			LucraftCoreHandler.registerEntry();
		}
		
		// Render layers ===========================================
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		for (RenderPlayer renderPlayer : skinMap.values()) {
			renderPlayer.addLayer(new LayerRegeneration(renderPlayer)); // Add Regeneration Layer
			renderPlayer.layerRenderers.removeIf(layer -> layer.getClass() == LayerHeldItem.class); // Remove old held item layer
			renderPlayer.addLayer(new LayerItemReplace(renderPlayer)); // Add new item layer
			renderPlayer.addLayer(new LayerFuzz(renderPlayer));
		}
		
	}
	
	@Override
	public void postInit() {
		super.postInit();
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		for (RenderPlayer renderPlayer : skinMap.values()) {
			RenderUtil.setupArmorModelOverride(renderPlayer);
		}
		
		//SkinChangingHandlerOLD.init(); NOW do initialisation somewhere else
	}
	
}
