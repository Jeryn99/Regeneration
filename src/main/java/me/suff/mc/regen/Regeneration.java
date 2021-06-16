package me.suff.mc.regen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.suff.mc.regen.common.advancements.TriggerManager;
import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.capability.RegenStorage;
import me.suff.mc.regen.common.commands.RegenCommand;
import me.suff.mc.regen.common.skin.HandleSkins;
import me.suff.mc.regen.common.traits.TraitManager;
import me.suff.mc.regen.compat.TardisCompat;
import me.suff.mc.regen.data.*;
import me.suff.mc.regen.handlers.CommonHandler;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.handlers.acting.ActingForwarder;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.proxy.ClientProxy;
import me.suff.mc.regen.proxy.CommonProxy;
import me.suff.mc.regen.proxy.Proxy;
import me.suff.mc.regen.util.client.ClientUtil;
import me.suff.mc.regen.util.common.PlayerUtil;
import me.suff.mc.regen.util.common.RegenUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

@Mod(Regeneration.MODID)
public class Regeneration {

    public static final String MODID = "regeneration";
    public static final String NAME = "Regeneration";

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Regeneration INSTANCE;
    public static Logger LOG = LogManager.getLogger(NAME);
    public static Proxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public Regeneration() {
        INSTANCE = this;
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.register(this);
        modBus.addListener(this::setup);
        modBus.addListener(this::enqueueIMC);
        modBus.addListener(this::processIMC);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(this::doClientStuff));
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CommonHandler());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientUtil.doClientStuff();
    }

    private void setup(final FMLCommonSetupEvent event) {
        proxy.preInit();
        CapabilityManager.INSTANCE.register(IRegen.class, new RegenStorage(), RegenCap::new);
        ActingForwarder.init();
        TriggerManager.init();
        RegenObjects.GallifreyBiomes.registerBiomeTypes();

        if (ModList.get().isLoaded("tardis") && RegenConfig.COMMON.tardisModCompatFeatures.get()) {
            TardisCompat.addTardisCompat();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onNewRegistries(RegistryEvent.NewRegistry e) {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RegenObjects.Blocks.BLOCKS.register(eventBus);
        RegenObjects.Blocks.BLOCK_ITEMS.register(eventBus);
        RegenObjects.Items.ITEMS.register(eventBus);
        RegenObjects.Sounds.SOUNDS.register(eventBus);
        RegenObjects.EntityEntries.ENTITIES.register(eventBus);
        RegenObjects.Tiles.TILES.register(eventBus);
        RegenObjects.Containers.CONTAINERS.register(eventBus);
        RegenObjects.ChunkGeneratorTypes.CHUNK_GENERATOR_TYPES.register(eventBus);
        RegenObjects.GallifreyBiomes.BIOMES.register(eventBus);
        RegenObjects.Dimensions.DIMENSIONS.register(eventBus);
    }


    @SubscribeEvent
    public void gatherData(GatherDataEvent e) {
        e.getGenerator().addProvider(new LangCreation(e.getGenerator()));
        e.getGenerator().addProvider(new RecipeCreation(e.getGenerator()));
        e.getGenerator().addProvider(new ItemsTagCreation(e.getGenerator()));
        e.getGenerator().addProvider(new BlockTagCreation(e.getGenerator()));
        e.getGenerator().addProvider(new LootTableCreation(e.getGenerator()));
        e.getGenerator().addProvider(new AdvancementCreation(e.getGenerator()));
    }


    private void enqueueIMC(final InterModEnqueueEvent event) {
        proxy.init();
        NetworkDispatcher.init();
        TraitManager.init();
    }

    private void processIMC(final InterModProcessEvent event) {
        proxy.postInit();
        PlayerUtil.createPostList();
        RegenUtil.TIMELORD_NAMES = new String[]{"Timelord"};

        try {
            File dir = new File("./regen_temp");
            if (dir.exists()) {
                FileUtils.cleanDirectory(new File("./regen_temp"));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        new Thread(() -> {
            try {
                HandleSkins.downloadTimelordSkins();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, Regeneration.NAME + " Timelord Download Daemon").start();
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event) {
        RegenCommand.register(event.getCommandDispatcher());
    }

}
