package com.lcm.regeneration.events;

import com.lcm.regeneration.common.capabilities.timelord.TimelordClientEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;


public class ClientProxy extends CommonProxy {

	@Override public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new TimelordClientEventHandler());
	}

	@Override public void postInit(FMLPostInitializationEvent ev) {
	//	ClientCommandHandler.instance.registerCommand(new GuiRegenCustomizer.CustomizeCommand());
	}


}
