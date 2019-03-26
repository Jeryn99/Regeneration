package me.suff.regeneration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.suff.regeneration.client.ClientEventHandler;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.debugger.IRegenDebugger;
import me.suff.regeneration.handlers.ActingForwarder;
import me.suff.regeneration.handlers.RegenEventHandler;
import me.suff.regeneration.network.NetworkHandler;
import me.suff.regeneration.proxy.ClientProxy;
import me.suff.regeneration.proxy.CommonProxy;
import me.suff.regeneration.proxy.IProxy;
import me.suff.regeneration.util.LimbManipulationUtil;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.nbt.INBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod("regeneration")
public class RegenerationMod {
	
	public static final String MODID = "regeneration";
	public static final String NAME = "Regeneration";
	public static final String VERSION = "0.0.4";
	
	public static final ResourceLocation LOOT_FILE = new ResourceLocation(MODID, "fob_watch_loot");
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static RegenerationMod INSTANCE;
	public static IRegenDebugger DEBUGGER;
	
	public static Logger LOG = LogManager.getLogger(NAME);
	
	public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	
	public RegenerationMod() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		
		INSTANCE = this;
		
		MinecraftForge.EVENT_BUS.register(new RegenEventHandler.AddCapabilities());
		MinecraftForge.EVENT_BUS.register(new RegenEventHandler());
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		MinecraftForge.EVENT_BUS.register(new SkinChangingHandler());
		MinecraftForge.EVENT_BUS.register(new LimbManipulationUtil());
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		proxy.preInit();
		CapabilityManager.INSTANCE.register(IRegeneration.class, new Capability.IStorage<IRegeneration>() {
			@Nullable
			@Override
			public INBTBase writeNBT(Capability<IRegeneration> capability, IRegeneration instance, EnumFacing side) {
				return null;
			}
			
			@Override
			public void readNBT(Capability<IRegeneration> capability, IRegeneration instance, EnumFacing side, INBTBase nbt) {
			
			}
		}, () -> null);
		ActingForwarder.init();
	}
	
	
	private void enqueueIMC(final InterModEnqueueEvent event) {
		proxy.init();
		NetworkHandler.init();
		LootTableList.register(LOOT_FILE);
	}
	
	private void processIMC(final InterModProcessEvent event) {
		proxy.postInit();
		PlayerUtil.createPostList();
	}
	
}
