package com.lcm.regeneration;

import com.lcm.regeneration.common.capabilities.timelord.capability.CapabilityRegeneration;
import com.lcm.regeneration.common.capabilities.timelord.capability.IRegenerationCapability;
import com.lcm.regeneration.events.CommonProxy;
import com.lcm.regeneration.networking.RNetwork;
import com.lcm.regeneration.utils.DebugCommand;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Regeneration.MODID, name = Regeneration.NAME, version = Regeneration.VERSION, dependencies = "required:forge@[14.23.1.2574,)", acceptedMinecraftVersions = "1.12.2")
@EventBusSubscriber
public class Regeneration {

	@SidedProxy(serverSide = "com.lcm.regeneration.events.CommonProxy", clientSide = "com.lcm.regeneration.events.ClientProxy")
	public static CommonProxy proxy;

	public static final String MODID = "lcm-regen";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "1.0";
	public static final ResourceLocation ICONS = new ResourceLocation(MODID, "textures/gui/ability_icons.png");

	@Mod.Instance(MODID)
	public static Regeneration INSTANCE;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		RNetwork.init();
		MinecraftForge.EVENT_BUS.register(proxy);
		CapabilityManager.INSTANCE.register(IRegenerationCapability.class, new CapabilityRegeneration.Storage(), CapabilityRegeneration.class);
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new DebugCommand());
	}

}
