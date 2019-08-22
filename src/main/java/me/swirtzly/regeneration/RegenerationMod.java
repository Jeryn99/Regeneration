package me.swirtzly.regeneration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.swirtzly.regeneration.client.rendering.entity.ItemOverrideRenderer;
import me.swirtzly.regeneration.client.rendering.entity.LindosRenderer;
import me.swirtzly.regeneration.common.advancements.TriggerManager;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.capability.RegenStorage;
import me.swirtzly.regeneration.common.commands.RegenDebugCommand;
import me.swirtzly.regeneration.common.entity.LindosEntity;
import me.swirtzly.regeneration.common.entity.OverrideEntity;
import me.swirtzly.regeneration.common.traits.TraitManager;
import me.swirtzly.regeneration.common.types.TypeManager;
import me.swirtzly.regeneration.handlers.CommonHandler;
import me.swirtzly.regeneration.handlers.acting.ActingForwarder;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.proxy.ClientProxy;
import me.swirtzly.regeneration.proxy.CommonProxy;
import me.swirtzly.regeneration.proxy.Proxy;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
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

@Mod(RegenerationMod.MODID)
public class RegenerationMod {
	
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "1.6.7";
	public static final String UPDATE_URL = "https://raw.githubusercontent.com/Suffril/Regeneration/skins/update.json";
	public static final String DEPS = "required:forge@[14.23.5.2768,);after:tardis@[0.0.7,];after:lucraftcore@[1.12.2-2.4.0,]";

	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


	public static RegenerationMod INSTANCE;

	public RegenerationMod(){
		INSTANCE = this;
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
		RenderingRegistry.registerEntityRenderingHandler(LindosEntity.class, LindosRenderer::new);
	}


	private void setup(final FMLCommonSetupEvent event) {
		proxy.preInit();
        CapabilityManager.INSTANCE.register(IRegen.class, new RegenStorage(), RegenCap::new);
		ActingForwarder.init();
		TriggerManager.init();
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		proxy.init();
		NetworkDispatcher.init();
		TraitManager.init();
		TypeManager.init();
	}


	private void processIMC(final InterModProcessEvent event) {
		proxy.postInit();
		PlayerUtil.createPostList();
	}
	
	@SubscribeEvent
	public void serverStart(FMLServerStartingEvent event) {
		RegenDebugCommand.register(event.getCommandDispatcher());
	}

	
}
