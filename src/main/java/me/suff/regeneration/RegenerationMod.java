package me.suff.regeneration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.suff.regeneration.common.advancements.RegenTriggers;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.dna.DnaHandler;
import me.suff.regeneration.debugger.IRegenDebugger;
import me.suff.regeneration.handlers.ActingForwarder;
import me.suff.regeneration.handlers.RegenEventHandler;
import me.suff.regeneration.network.NetworkHandler;
import me.suff.regeneration.proxy.ClientProxy;
import me.suff.regeneration.proxy.CommonProxy;
import me.suff.regeneration.proxy.IProxy;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
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
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

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
	
	public static final IProxy PROXY = DistExecutor.runForDist( () -> ClientProxy::new, () -> CommonProxy::new );
	
	public RegenerationMod() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, RegenConfig.CONFIG_SPEC);
		INSTANCE = this;
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new CapabilityRegeneration());
		MinecraftForge.EVENT_BUS.register(new RegenEventHandler());
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		
		CapabilityManager.INSTANCE.register(IRegeneration.class, new Capability.IStorage<IRegeneration>() {
			@Nullable
			@Override
			public INBTBase writeNBT(Capability<IRegeneration> capability, IRegeneration instance, EnumFacing side) {
				return instance.serializeNBT();
			}
			
			@Override
			public void readNBT(Capability<IRegeneration> capability, IRegeneration instance, EnumFacing side, INBTBase nbt) {
				instance.deserializeNBT(nbt instanceof NBTTagCompound ? (NBTTagCompound) nbt : new NBTTagCompound());
			}
		}, CapabilityRegeneration::new);
		
		
		ActingForwarder.init();
		RegenTriggers.init();
		NetworkHandler.init();
		LootTableList.register(LOOT_FILE);
		DnaHandler.init();
		PlayerUtil.createPostList();
		PROXY.preInit();
	}
	
	private void enqueueIMC(final InterModEnqueueEvent event) {
		PROXY.init();
	}
	
	private void processIMC(final InterModProcessEvent event) {
		PROXY.postInit();
	}
	
}
