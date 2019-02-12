package me.suff.regeneration;

import com.google.common.eventbus.Subscribe;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

//TESTING add language file tests
@Mod(RegenerationMod.MODID)
public class RegenerationMod {
	
	public RegenerationMod(){
		FMLModLoadingContext.get().getModEventBus().addListener(this::setup);
	}
	
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "1.4.6";
	
	public static final ResourceLocation LOOT_FILE = new ResourceLocation(MODID, "fob_watch_loot");
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static IRegenDebugger DEBUGGER;
	
	public static Logger LOG = LogManager.getLogger(NAME);
	
	
	private void setup(final FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(IRegeneration.class, new RegenerationStorage(), CapabilityRegeneration::new);
		ActingForwarder.init();
		RegenTriggers.init();
		
		NetworkHandler.init();
		LootTableList.register(LOOT_FILE);
		DnaHandler.init();
	}
	
	//@EventHandler
	//public void postInit(FMLPostInitializationEvent e) {
	//	proxy.postInit();
	//	PlayerUtil.createPostList();
	//}
	
	
}
