package me.swirtzly.regeneration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import me.swirtzly.regeneration.util.common.PlayerUtil;
import me.swirtzly.regeneration.util.common.RegenUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Mod(Regeneration.MODID)
public class Regeneration {
	
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Regeneration INSTANCE;

    public Regeneration() {
		INSTANCE = this;
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new CommonHandler());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);
	}
	
	public static Logger LOG = LogManager.getLogger(NAME);

    public static Proxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    private void doClientStuff(final FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(OverrideEntity.class, ItemOverrideRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TimelordEntity.class, TimelordRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(LaserEntity.class, LaserRenderer::new);
	}

    private void setup(final FMLCommonSetupEvent event) {
		proxy.preInit();
        CapabilityManager.INSTANCE.register(IRegen.class, new RegenStorage(), RegenCap::new);
		ActingForwarder.init();
		TriggerManager.init();
		RegenObjects.Biomes.registerBiomeTypes();
	}
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
	public void onNewRegistries(RegistryEvent.NewRegistry e) {
    	RegenObjects.Blocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegenObjects.Blocks.BLOCK_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegenObjects.Items.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegenObjects.Sounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegenObjects.EntityEntries.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegenObjects.Tiles.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegenObjects.Containers.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegenObjects.ChunkGeneratorTypes.CHUNK_GENERATOR_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegenObjects.Biomes.BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegenObjects.Dimensions.DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());

	}

    private void enqueueIMC(final InterModEnqueueEvent event) {
		proxy.init();
		NetworkDispatcher.init();
		TraitManager.init();


		/* This is bad dumb code, but in order to release at the moment, it needs to exist
		 * in future versions, this will be gutted out and version comparisons like this should never
		 * be used again, it is just a temp thing
		 */
		if (ModList.get().isLoaded("tardis")) {
			Optional<? extends ModContainer> optionalModContainer = ModList.get().getModContainerById("tardis");
			ModContainer tardisContainer = optionalModContainer.get();
			ArtifactVersion version = tardisContainer.getModInfo().getVersion();
			if (version.getMajorVersion() == 1 && version.getMinorVersion() >= 3) {
				TardisCompat.addTardisCompat();
			} else {
				LOG.error("Version " + version.getMajorVersion() + "." + version.getMinorVersion() + " is too low for use with the compatible features of Regeneration, please be on at minimum: 1.3");
			}
		}
	}

    private void processIMC(final InterModProcessEvent event) {
		proxy.postInit();
		PlayerUtil.createPostList();

		RegenUtil.TIMELORD_NAMES = RegenUtil.downloadNames();
        HandleSkins.downloadSkins();
        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                HandleSkins.downloadSkins();
            }
        };

        timer.schedule(hourlyTask, 0L, 1000 * 60 * 60);
	}
	
	@SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event) {
		RegenCommand.register(event.getCommandDispatcher());
	}
	
}
