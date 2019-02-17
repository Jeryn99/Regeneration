package me.suff.regeneration.proxy;

import me.suff.regeneration.client.RegenKeyBinds;
import me.suff.regeneration.client.gui.InventoryTabRegeneration;
import me.suff.regeneration.client.rendering.LayerFuzz;
import me.suff.regeneration.client.rendering.LayerItemReplace;
import me.suff.regeneration.client.rendering.LayerRegeneration;
import me.suff.regeneration.client.rendering.entity.RenderItemOverride;
import me.suff.regeneration.client.rendering.entity.RenderLindos;
import me.suff.regeneration.client.rendering.model.ModelArmorOverride;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.common.entity.EntityItemOverride;
import me.suff.regeneration.common.entity.EntityLindos;
import me.suff.regeneration.util.FileUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
	//	MinecraftForge.EVENT_BUS.register(new SkinChangingHandler());
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
		Map<String, RenderPlayer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
		for (RenderPlayer renderPlayer : skinMap.values()) {
			
			//List<LayerRenderer<AbstractClientPlayer>> ita = renderPlayer.layerRenderers;
		//	for (LayerRenderer layer : ita) {
		//		if (layer instanceof LayerArmorBase) { //Armor Layer
		//			LayerArmorBase l = (LayerArmorBase) layer;
		//			ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, l, new ModelArmorOverride(), 1);
		//			ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, l, new ModelArmorOverride(), 2);
		//		}
				
	//			if (layer instanceof LayerHeldItem) {
	//				renderPlayer.addLayer(new LayerItemReplace(renderPlayer)); // Add new item layer
	//			}
	//		}
			
			renderPlayer.addLayer(new LayerRegeneration(renderPlayer)); // Add Regeneration Layer
			renderPlayer.addLayer(new LayerFuzz(renderPlayer));
		}
		
		try {
			FileUtil.createDefaultFolders();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
