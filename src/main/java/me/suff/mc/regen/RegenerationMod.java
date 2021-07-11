package me.suff.mc.regen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.suff.mc.regen.client.gui.GuiHandler;
import me.suff.mc.regen.common.RegenPermission;
import me.suff.mc.regen.common.advancements.RegenTriggers;
import me.suff.mc.regen.common.capability.CapabilityRegeneration;
import me.suff.mc.regen.common.capability.IRegeneration;
import me.suff.mc.regen.common.capability.RegenerationStorage;
import me.suff.mc.regen.common.commands.RegenDebugCommand;
import me.suff.mc.regen.common.item.arch.capability.ArchStorage;
import me.suff.mc.regen.common.item.arch.capability.CapabilityArch;
import me.suff.mc.regen.common.item.arch.capability.IArch;
import me.suff.mc.regen.common.tiles.TileEntityHandInJar;
import me.suff.mc.regen.common.traits.DnaHandler;
import me.suff.mc.regen.common.types.TypeHandler;
import me.suff.mc.regen.compat.lucraft.LucraftCoreHandler;
import me.suff.mc.regen.compat.tardis.TardisModHandler;
import me.suff.mc.regen.handlers.ActingForwarder;
import me.suff.mc.regen.network.NetworkHandler;
import me.suff.mc.regen.proxy.CommonProxy;
import me.suff.mc.regen.util.EnumCompatModids;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = RegenerationMod.MODID, name = RegenerationMod.NAME, version = RegenerationMod.VERSION, updateJSON = RegenerationMod.UPDATE_URL, dependencies = RegenerationMod.DEPS)
public class RegenerationMod {

    public static final String MODID = "regeneration";
    public static final String NAME = "Regeneration";
    public static final String VERSION = "3.0.0";
    public static final String UPDATE_URL = "https://raw.githubusercontent.com/Swirtzly/Regeneration/skins/update.json";
    public static final String DEPS = "required:forge@[14.23.5.2768,);after:tardis@[0.0.7,];after:lucraftcore@[1.12.2-2.4.0,]";

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Mod.Instance(MODID)
    public static RegenerationMod INSTANCE;

    public static Logger LOG = LogManager.getLogger(NAME);

    @SidedProxy(clientSide = "me.suff.mc.regen.proxy.ClientProxy", serverSide = "me.suff.mc.regen.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static boolean isDevEnv() {
        return (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        CapabilityManager.INSTANCE.register(IRegeneration.class, new RegenerationStorage(), CapabilityRegeneration::new);
        CapabilityManager.INSTANCE.register(IArch.class, new ArchStorage(), CapabilityArch::new);

        ActingForwarder.init();
        RegenTriggers.init();

        if (EnumCompatModids.TARDIS.isLoaded()) {
            LOG.info("Tardis mod Detected - Enabling Compatibility");
            ActingForwarder.register(TardisModHandler.class, Side.SERVER);
            TardisModHandler.registerEventBus();

        }

        if (EnumCompatModids.LCCORE.isLoaded()) {
            LOG.info("Lucraft Core Detected - Enabling Compatibility");
            ActingForwarder.register(LucraftCoreHandler.class, Side.SERVER);
            LucraftCoreHandler.registerEventBus();
        }

        GameRegistry.registerTileEntity(TileEntityHandInJar.class, new ResourceLocation(MODID, "handinjar"));
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        NetworkHandler.init();
        DnaHandler.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
        TypeHandler.init();
        RegenPermission.registerPermissions();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit();
        PlayerUtil.createPostList();
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new RegenDebugCommand());
    }
	
}
