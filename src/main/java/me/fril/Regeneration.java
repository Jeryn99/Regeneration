package me.fril;

import me.fril.common.capability.CapabilityRegeneration;
import me.fril.common.states.RegenTypes;
import me.fril.network.NetworkHandler;
import me.fril.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Regeneration.MODID, name = Regeneration.NAME, version = Regeneration.VERSION, updateJSON = Regeneration.UPDATE_URL)
public class Regeneration {
    public static final String MODID = "regeneration";
    public static final String NAME = "Regeneration";
    public static final String VERSION = "1.0.0";
    public static final String UPDATE_URL = "https://github.com/Suffril/Regeneration/blob/master/update.json";

    @Mod.Instance(MODID)
    public static Regeneration INSTANCE;

    @SidedProxy(clientSide = "me.fril.proxy.ClientProxy", serverSide = "me.fril.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static Logger LOG = LogManager.getLogger(NAME);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        CapabilityRegeneration.init();
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
