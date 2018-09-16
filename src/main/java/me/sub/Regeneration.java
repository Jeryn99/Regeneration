package me.sub;

import me.sub.common.capability.CapabilityRegeneration;
import me.sub.network.NetworkHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Regeneration.MODID, name = Regeneration.NAME, version = Regeneration.VERSION)
public class Regeneration {
    public static final String MODID = "regeneration";
    public static final String NAME = "Regeneration";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static Regeneration INSTANCE;

    public static Logger LOG = LogManager.getLogger(NAME);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityRegeneration.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkHandler.init();
    }
}
