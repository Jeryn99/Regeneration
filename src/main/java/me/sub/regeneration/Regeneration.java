package me.sub.regeneration;

import me.sub.regeneration.common.capability.CapabilityRegeneration;
import me.sub.regeneration.common.capability.IRegenerationCapability;
import me.sub.regeneration.common.commands.CommandDebug;
import me.sub.regeneration.networking.RNetwork;
import me.sub.regeneration.proxy.CommonProxy;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Regeneration.MODID, name = Regeneration.NAME, version = Regeneration.VERSION, dependencies = "required:forge@[14.23.1.2574,)", acceptedMinecraftVersions = "1.12.2", updateJSON = Regeneration.UPDATE_JSON)
public class Regeneration {

	@SidedProxy(serverSide = "me.sub.regeneration.proxy.CommonProxy", clientSide = "me.sub.regeneration.proxy.ClientProxy")
	public static CommonProxy proxy;

	public static final String MODID = "lcm-regen";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "a4";
	public static final String UPDATE_JSON = "https://github.com/SandedShoes/Regeneration/blob/master-1.12.2/update.json";
	
	@Mod.Instance(MODID)
	public static Regeneration INSTANCE;

    Logger LOG = LogManager.getLogger(NAME);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		RNetwork.init();
		CapabilityManager.INSTANCE.register(IRegenerationCapability.class, new CapabilityRegeneration.Storage(), CapabilityRegeneration::new);
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDebug());
	}

    @EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        LOG.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }

}
