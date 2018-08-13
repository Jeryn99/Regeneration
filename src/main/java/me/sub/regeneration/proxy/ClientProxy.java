package me.sub.regeneration.proxy;

import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabRegeneration;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		super.preInit();
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void postInit() {
		if (TabRegistry.getTabList().size() < 2){
        	TabRegistry.registerTab(new InventoryTabVanilla());
        }
    	TabRegistry.registerTab(new InventoryTabRegeneration());
	}
}
