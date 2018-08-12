package com.lcm.regeneration.events;

import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabRegeneration;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override public void postInit(FMLPostInitializationEvent ev) {
		if (TabRegistry.getTabList().size() < 2){
        	TabRegistry.registerTab(new InventoryTabVanilla());
        }
    	TabRegistry.registerTab(new InventoryTabRegeneration());

	}


}
