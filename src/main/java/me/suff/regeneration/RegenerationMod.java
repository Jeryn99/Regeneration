package me.suff.regeneration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.suff.regeneration.client.gui.GuiHandler;
import me.suff.regeneration.common.advancements.RegenTriggers;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.capability.RegenerationStorage;
import me.suff.regeneration.common.commands.RegenDebugCommand;
import me.suff.regeneration.common.dna.DnaHandler;
import me.suff.regeneration.compat.lucraft.LucraftCoreHandler;
import me.suff.regeneration.compat.tardis.TardisModHandler;
import me.suff.regeneration.debugger.DummyRegenDebugger;
import me.suff.regeneration.debugger.GraphicalRegenDebugger;
import me.suff.regeneration.debugger.IRegenDebugger;
import me.suff.regeneration.handlers.ActingForwarder;
import me.suff.regeneration.network.NetworkHandler;
import me.suff.regeneration.proxy.CommonProxy;
import me.suff.regeneration.util.EnumCompatModids;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

@Mod(modid = RegenerationMod.MODID, name = RegenerationMod.NAME, version = RegenerationMod.VERSION, updateJSON = RegenerationMod.UPDATE_URL, dependencies = RegenerationMod.DEPS)
public class RegenerationMod {
	
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "1.5.0b";
	public static final String UPDATE_URL = "https://raw.githubusercontent.com/Suffril/Regeneration/master/update.json";
	public static final String DEPS = "required:forge@[14.23.5.2768,);after:tardis@[0.0.7,];after:lucraftcore@[1.12.2-2.4.0,]";
	
	public static final ResourceLocation LOOT_FILE = new ResourceLocation(MODID, "fob_watch_loot");
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	@Mod.Instance(MODID)
	public static RegenerationMod INSTANCE;
	public static IRegenDebugger DEBUGGER;
	
	public static Logger LOG = LogManager.getLogger(NAME);
	
	@SidedProxy(clientSide = "me.suff.regeneration.proxy.ClientProxy", serverSide = "me.suff.regeneration.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
		CapabilityManager.INSTANCE.register(IRegeneration.class, new RegenerationStorage(), CapabilityRegeneration::new);
		
		ActingForwarder.init();
		RegenTriggers.init();
		
		if (EnumCompatModids.TARDIS.isLoaded()) {
			LOG.info("Tardis mod Detected - Enabling Compat");
			ActingForwarder.register(TardisModHandler.class, Side.SERVER);
		}
		
		if (EnumCompatModids.LCCORE.isLoaded()) {
			LOG.info("Lucraft Core Detected - Enabling Compat");
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
		PlayerUtil.createPostList();
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
