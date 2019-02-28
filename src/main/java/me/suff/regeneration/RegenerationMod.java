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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TESTING add language file tests
@Mod(RegenerationMod.MODID)
public class RegenerationMod {
	
	public RegenerationMod(){
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RegenConfig.CONFIG_SPEC);
	}
	
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "1.5.0";
	
	public static final ResourceLocation LOOT_FILE = new ResourceLocation(MODID, "fob_watch_loot");
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
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
	
	
}
