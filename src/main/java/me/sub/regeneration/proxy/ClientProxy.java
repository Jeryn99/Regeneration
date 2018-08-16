package me.sub.regeneration.proxy;

import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabRegeneration;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.List;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		super.preInit();
	}

	@Override
	public void postInit() {
		if (TabRegistry.getTabList().size() < 2){
        	TabRegistry.registerTab(new InventoryTabVanilla());
        }
    	TabRegistry.registerTab(new InventoryTabRegeneration());
	}

    @Override
    public void init() {
        super.init();
        itemFix();
    }

    public void itemFix() {
        for (RenderPlayer playerRender : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
            List<LayerRenderer> list = ReflectionHelper.getPrivateValue(RenderLivingBase.class, playerRender, "layerRenderers", "field_177097_h");
            list.removeIf(layer -> layer instanceof LayerHeldItem);
        }
    }
	 
}
