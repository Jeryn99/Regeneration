package me.suff.regeneration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.suff.regeneration.client.RegenKeyBinds;
import me.suff.regeneration.client.rendering.entity.RenderItemOverride;
import me.suff.regeneration.client.rendering.entity.RenderLindos;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.common.advancements.RegenTriggers;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.capability.RegenerationStorage;
import me.suff.regeneration.common.dna.DnaHandler;
import me.suff.regeneration.common.entity.EntityItemOverride;
import me.suff.regeneration.common.entity.EntityLindos;
import me.suff.regeneration.debugger.IRegenDebugger;
import me.suff.regeneration.handlers.ActingForwarder;
import me.suff.regeneration.handlers.RegenObjects;
import me.suff.regeneration.network.NetworkHandler;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TESTING add language file tests
@Mod(RegenerationMod.MODID)
public class RegenerationMod {
	
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "1.4.6";
	public static final ResourceLocation LOOT_FILE = new ResourceLocation(MODID, "fob_watch_loot");
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static Object INSTANCE = null;
	public static IRegenDebugger DEBUGGER;
	public static Logger LOG = LogManager.getLogger(NAME);
	
	public RegenerationMod() {
		INSTANCE = this;
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new CapabilityRegeneration());
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RegenConfig.CONFIG_SPEC);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(IRegeneration.class, new RegenerationStorage(), CapabilityRegeneration::new);
		ActingForwarder.init();
		RegenTriggers.init();
		NetworkHandler.init();
		LootTableList.register(LOOT_FILE);
		DnaHandler.init();
		PlayerUtil.createPostList();
		RegenObjects.EntityEntries.init();
	}
	
	@OnlyIn(Dist.CLIENT) //Not sure if this annotation needs to exist with this
	private void doClientStuff(final FMLClientSetupEvent event) {
		RegenKeyBinds.init();
		MinecraftForge.EVENT_BUS.register(new SkinChangingHandler());
		RenderingRegistry.registerEntityRenderingHandler(EntityItemOverride.class, RenderItemOverride::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLindos.class, RenderLindos::new);
	}
	
	
}
