package me.fril.regeneration;

import me.fril.regeneration.client.gui.GuiHandler;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.capability.RegenerationStorage;
import me.fril.regeneration.common.commands.RegenDebugCommand;
import me.fril.regeneration.debugger.RegenDebugger;
import me.fril.regeneration.handlers.ActingForwarder;
import me.fril.regeneration.handlers.TardisModHandler;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.proxy.CommonProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

//TODO add language file tests
@Mod(modid = Regeneration.MODID, name = Regeneration.NAME, version = Regeneration.VERSION, updateJSON = Regeneration.UPDATE_URL, dependencies = "required:forge@[14.23.5.2768,);after:tardis")
public class Regeneration {
	
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "beta-v1.3-29c2dd3";
	public static final String UPDATE_URL = "https://raw.githubusercontent.com/Suffril/Regeneration/master/update.json";
	
	public static final ResourceLocation LOOT_FILE = new ResourceLocation(MODID, "fob_watch_loot");
	
	@Mod.Instance(MODID)
    public static Regeneration INSTANCE;
	public static RegenDebugger DEBUGGER;
	
	@SidedProxy(clientSide = "me.fril.regeneration.proxy.ClientProxy", serverSide = "me.fril.regeneration.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
		CapabilityManager.INSTANCE.register(IRegeneration.class, new RegenerationStorage(), CapabilityRegeneration::new);
		ActingForwarder.init();

        if (Loader.isModLoaded("tardis")) {
			ActingForwarder.register(TardisModHandler.class, Side.SERVER);
		}

    }
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		NetworkHandler.init();
		LootTableList.register(LOOT_FILE);
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit();
	}
	
	
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new RegenDebugCommand());
		
		DEBUGGER = new RegenDebugger();
		MinecraftForge.EVENT_BUS.register(DEBUGGER);
	}
	
	@EventHandler
	public void serverStop(FMLServerStoppingEvent event) {
		MinecraftForge.EVENT_BUS.unregister(DEBUGGER);
		DEBUGGER.dispose();
		DEBUGGER = null;
	}
	
}
