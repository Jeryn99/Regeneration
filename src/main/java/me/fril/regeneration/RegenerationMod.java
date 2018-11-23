package me.fril.regeneration;

import me.fril.regeneration.client.gui.GuiHandler;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.capability.RegenerationStorage;
import me.fril.regeneration.common.types.RegenTypes;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.proxy.CommonProxy;
import me.fril.regeneration.util.RegenDebugCommand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = RegenerationMod.MODID, name = RegenerationMod.NAME, version = RegenerationMod.VERSION, updateJSON = RegenerationMod.UPDATE_URL)
public class RegenerationMod {
	
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "1.2.4";
	public static final String UPDATE_URL = "https://raw.githubusercontent.com/Suffril/Regeneration/master/update.json";
	
	public static final ResourceLocation LOOT_FILE = new ResourceLocation(MODID, "fob_watch_loot");
	
	@Mod.Instance(MODID)
	public static RegenerationMod INSTANCE;
	
	@SidedProxy(clientSide = "me.fril.regeneration.proxy.ClientProxy", serverSide = "me.fril.regeneration.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
		CapabilityManager.INSTANCE.register(IRegeneration.class, new RegenerationStorage(), CapabilityRegeneration::new);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		NetworkHandler.init();
		RegenTypes.init();
		LootTableList.register(LOOT_FILE);
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
	}
	
	@EventHandler
	public void serverInit(FMLServerStartingEvent event) {
		event.registerServerCommand(new RegenDebugCommand());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit();
	}
	
}
