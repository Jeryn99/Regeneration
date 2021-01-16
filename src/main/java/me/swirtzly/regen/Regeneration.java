package me.swirtzly.regen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.swirtzly.regen.common.entities.TimelordEntity;
import me.swirtzly.regen.common.objects.*;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.RegenStorage;
import me.swirtzly.regen.common.regen.acting.ActingForwarder;
import me.swirtzly.regen.common.world.gen.RStructures;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.data.BlockstateGen;
import me.swirtzly.regen.data.EnglishLangGen;
import me.swirtzly.regen.data.RRecipeGen;
import me.swirtzly.regen.data.RSoundsGen;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.util.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("regen")
public class Regeneration {

    public static final Logger LOG = LogManager.getLogger("Regeneration");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public Regeneration() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doCommonStuff);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        NetworkDispatcher.setUp();
        PlayerUtil.setupPotions();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RegenConfig.SKIN_SPEC, "regen-skin.toml");
    }


    private void doCommonStuff(final FMLCommonSetupEvent event) {

        event.enqueueWork(() ->
        {
            RStructures.setupStructures();
            RStructures.ConfiguredStructures.registerConfiguredStructures();
        });

        CapabilityManager.INSTANCE.register(IRegen.class, new RegenStorage(), RegenCap::new);
        ActingForwarder.init();
        GlobalEntityTypeAttributes.put(REntities.TIMELORD.get(), TimelordEntity.createAttributes().create());
        GlobalEntityTypeAttributes.put(REntities.WATCHER.get(), TimelordEntity.createAttributes().create());
        DownloadSkinsThread.setup(FMLEnvironment.dist == Dist.CLIENT);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientUtil::doClientStuff);
    }

    @SubscribeEvent
    public void onGatherData(GatherDataEvent e) {
        DataGenerator generator = e.getGenerator();
        generator.addProvider(new EnglishLangGen(generator));
        generator.addProvider(new RSoundsGen(generator));
        generator.addProvider(new RRecipeGen(generator));
        generator.addProvider(new BlockstateGen(generator));
    }

    @SubscribeEvent
    public void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        RSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        REntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        RBlocks.BLOCK_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RTiles.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        RStructures.Structures.STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void registerRecipeSerialziers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        CraftingHelper.register(new ResourceLocation(RConstants.MODID, "nbt"), NBTRecipeIngredient.Serializer.INSTANCE);
    }
}
