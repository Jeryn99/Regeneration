package me.fril.regeneration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.capability.RegenerationStorage;
import me.fril.regeneration.common.states.RegenTypes;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.proxy.CommonProxy;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = RegenerationMod.MODID, name = RegenerationMod.NAME, version = RegenerationMod.VERSION, updateJSON = RegenerationMod.UPDATE_URL)
public class RegenerationMod {
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "1.0.0";
	public static final String UPDATE_URL = "https://github.com/Suffril/Regeneration/blob/master/update.json";
	
	@Mod.Instance(MODID)
	public static RegenerationMod INSTANCE;
	
	@SidedProxy(clientSide = "me.fril.regeneration.proxy.ClientProxy", serverSide = "me.fril.regeneration.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	public static Logger LOG = LogManager.getLogger(NAME); //XXX unused
	
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
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit();
	}
	
}