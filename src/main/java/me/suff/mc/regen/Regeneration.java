package me.suff.mc.regen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.suff.mc.regen.client.rendering.entity.CyberRender;
import me.suff.mc.regen.client.rendering.entity.RenderLaser;
import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.client.rendering.entity.WatcherRenderer;
import me.suff.mc.regen.common.advancement.TriggerManager;
import me.suff.mc.regen.common.entities.Timelord;
import me.suff.mc.regen.common.entities.Watcher;
import me.suff.mc.regen.common.objects.*;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.acting.ActingForwarder;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.common.traits.RegenTraitRegistry;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.data.*;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.util.ClientUtil;
import me.suff.mc.regen.util.DownloadSkinsThread;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("regen")
public class Regeneration {

    public static final Logger LOG = LogManager.getLogger("Regeneration");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public Regeneration() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff));
        modBus.addListener(this::doCommonStuff);
        modBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);
        RBlocks.BLOCKS.register(modBus);
        RSounds.SOUNDS.register(modBus);
        RItems.ITEMS.register(modBus);
        REntities.ENTITIES.register(modBus);


        RTiles.TILES.register(modBus);
        RParticles.TYPES.register(modBus);
        RGlobalLoot.GLM.register(modBus);
        RegenTraitRegistry.TRAITS.register(modBus);
        TransitionTypes.TRANSITION_TYPES.register(modBus);
   //     RFeatures.DEFERRED_REGISTRY_STRUCTURE.register(modBus);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        // forgeBus.addListener(EventPriority.NORMAL, RWorldHelper::addDimensionalSpacing);

        NetworkDispatcher.init();
        PlayerUtil.setupPotions();
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
    }

    @SubscribeEvent
    public static void onAddCaps(RegisterCapabilitiesEvent capabilitiesEvent) {
        capabilitiesEvent.register(IRegen.class);
    }

    @SubscribeEvent
    public static void onAttributes(EntityAttributeCreationEvent attributeCreationEvent) {
        attributeCreationEvent.put(REntities.TIMELORD.get(), Timelord.createAttributes().build());
        attributeCreationEvent.put(REntities.WATCHER.get(), Watcher.createAttributes().build());
        attributeCreationEvent.put(REntities.CYBERLORD.get(), Timelord.createAttributes().build());
    }

    private void doCommonStuff(final FMLCommonSetupEvent event) {
        ActingForwarder.init();
        DownloadSkinsThread.setup();
        RSoundSchemes.init();
        TriggerManager.init();
        event.enqueueWork(() ->
        {
            //   RFeatures.ores();
            //    RFeatures.setupStructures();
            //    RWorldHelper.registerConfiguredStructures();
        });
    }

    @SubscribeEvent
    public void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(REntities.TIMELORD.get(), TimelordRenderer::new);
        event.registerEntityRenderer(REntities.WATCHER.get(), WatcherRenderer::new);
        event.registerEntityRenderer(REntities.LASER.get(), RenderLaser::new);
        event.registerEntityRenderer(REntities.CYBERLORD.get(), CyberRender::new);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientUtil::doClientStuff);
    }

    @SubscribeEvent
    public void onGatherData(GatherDataEvent e) {
        DataGenerator generator = e.getGenerator();
        ExistingFileHelper existingFileHelper = e.getExistingFileHelper();
        generator.addProvider(new EnglishLang(generator));
        generator.addProvider(new RBlockLootTableGen(generator));
        generator.addProvider(new LootGen(generator));
        RBlockTags blockTags = new RBlockTags(generator, existingFileHelper);
        generator.addProvider(blockTags);
        generator.addProvider(new RItemTags(generator, blockTags, existingFileHelper));
        generator.addProvider(new RRecipeGen(generator));
        generator.addProvider(new AdvancementGen(generator));
    }

}
