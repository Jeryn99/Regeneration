package me.fril.regeneration;

import me.fril.regeneration.client.gui.GuiHandler;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.capability.RegenerationStorage;
import me.fril.regeneration.common.commands.RegenDebugCommand;
import me.fril.regeneration.common.dna.DnaHandler;
import me.fril.regeneration.compat.lucraft.LucraftCoreHandler;
import me.fril.regeneration.compat.tardis.TardisModHandler;
import me.fril.regeneration.debugger.DummyRegenDebugger;
import me.fril.regeneration.debugger.GraphicalRegenDebugger;
import me.fril.regeneration.debugger.IRegenDebugger;
import me.fril.regeneration.handlers.ActingForwarder;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.proxy.CommonProxy;
import me.fril.regeneration.util.EnumCompatModids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

//TESTING add language file tests
@Mod(modid = RegenerationMod.MODID, name = RegenerationMod.NAME, version = RegenerationMod.VERSION, updateJSON = RegenerationMod.UPDATE_URL, dependencies = "required:forge@[14.23.5.2768,);after:tardis")
public class RegenerationMod {
	
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "1.3.8";
	public static final String UPDATE_URL = "https://raw.githubusercontent.com/Suffril/Regeneration/master/update.json";
	
	public static final ResourceLocation LOOT_FILE = new ResourceLocation(MODID, "fob_watch_loot");
	
	@Mod.Instance(MODID)
	public static RegenerationMod INSTANCE;
	public static IRegenDebugger DEBUGGER;
	
	public static Logger LOG = LogManager.getLogger(NAME);
	
	@SidedProxy(clientSide = "me.fril.regeneration.proxy.ClientProxy", serverSide = "me.fril.regeneration.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
		CapabilityManager.INSTANCE.register(IRegeneration.class, new RegenerationStorage(), CapabilityRegeneration::new);
		
		ActingForwarder.init();
		
		if (EnumCompatModids.TARDIS.isLoaded()) {
			ActingForwarder.register(TardisModHandler.class, Side.SERVER);
		}
		
		if (EnumCompatModids.LCCORE.isLoaded()) {
			ActingForwarder.register(LucraftCoreHandler.class, Side.SERVER);
			LucraftCoreHandler.registerEventBus();
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		NetworkHandler.init();
		LootTableList.register(LOOT_FILE);
		DnaHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit();
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new RegenDebugCommand());
		
		DEBUGGER = GraphicsEnvironment.isHeadless() ? new DummyRegenDebugger() : new GraphicalRegenDebugger();
		MinecraftForge.EVENT_BUS.register(DEBUGGER);
	}
	
	@EventHandler
	public void serverStop(FMLServerStoppingEvent event) {
		MinecraftForge.EVENT_BUS.unregister(DEBUGGER);
		DEBUGGER.dispose();
		DEBUGGER = null;
	}
	
}
