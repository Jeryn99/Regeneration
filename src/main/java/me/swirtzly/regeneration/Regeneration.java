package me.swirtzly.regeneration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.swirtzly.data.*;
import me.swirtzly.regeneration.client.rendering.entity.ItemOverrideRenderer;
import me.swirtzly.regeneration.client.rendering.entity.LaserRenderer;
import me.swirtzly.regeneration.client.rendering.entity.TimelordRenderer;
import me.swirtzly.regeneration.common.advancements.TriggerManager;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.capability.RegenStorage;
import me.swirtzly.regeneration.common.commands.RegenCommand;
import me.swirtzly.regeneration.common.entity.LaserEntity;
import me.swirtzly.regeneration.common.entity.OverrideEntity;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import me.swirtzly.regeneration.common.skin.HandleSkins;
import me.swirtzly.regeneration.common.traits.TraitManager;
import me.swirtzly.regeneration.compat.TardisCompat;
import me.swirtzly.regeneration.handlers.CommonHandler;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.handlers.acting.ActingForwarder;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.proxy.ClientProxy;
import me.swirtzly.regeneration.proxy.CommonProxy;
import me.swirtzly.regeneration.proxy.Proxy;
import me.swirtzly.regeneration.util.client.ClientUtil;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import me.swirtzly.regeneration.util.common.RegenUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.RegenPrefTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
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
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
        RegenUtil.TIMELORD_NAMES = RegenUtil.downloadNames();

        try {
            File dir = new File("./regen_temp");
            if(dir.exists()) {
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
