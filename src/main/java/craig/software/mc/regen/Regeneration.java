package craig.software.mc.regen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import craig.software.mc.regen.client.rendering.entity.CyberRender;
import craig.software.mc.regen.client.rendering.entity.RenderLaser;
import craig.software.mc.regen.client.rendering.entity.TimelordRenderer;
import craig.software.mc.regen.client.rendering.entity.WatcherRenderer;
import craig.software.mc.regen.common.advancement.TriggerManager;
import craig.software.mc.regen.common.commands.RegenCommands;
import craig.software.mc.regen.common.entities.Timelord;
import craig.software.mc.regen.common.entities.Watcher;
import craig.software.mc.regen.common.objects.*;
import craig.software.mc.regen.common.regen.IRegen;
import craig.software.mc.regen.common.regen.acting.ActingForwarder;
import craig.software.mc.regen.common.regen.transitions.TransitionTypes;
import craig.software.mc.regen.common.traits.RegenTraitRegistry;
import craig.software.mc.regen.common.world.RFeatures;
import craig.software.mc.regen.common.world.structures.pieces.StructurePieces;
import craig.software.mc.regen.config.RegenConfig;
import craig.software.mc.regen.data.*;
import craig.software.mc.regen.network.NetworkDispatcher;
import craig.software.mc.regen.util.ClientUtil;
import craig.software.mc.regen.util.DownloadSkinsThread;
import craig.software.mc.regen.util.PlayerUtil;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.data.event.GatherDataEvent;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
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

        RegenCommands.COMMAND_ARGUMENT_TYPES.register(modBus);
        RTiles.TILES.register(modBus);
        RParticles.TYPES.register(modBus);
        RGlobalLoot.GLM.register(modBus);
        RegenTraitRegistry.TRAITS.register(modBus);
        TransitionTypes.TRANSITION_TYPES.register(modBus);
        RFeatures.DEFERRED_REGISTRY_STRUCTURE.register(modBus);

        final DeferredRegister<Codec<? extends BiomeModifier>> serializers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, RConstants.MODID);
        serializers.register(modBus);

        RFeatures.CONFIGURED_FEATURES.register(modBus);
        RFeatures.PLACED_FEATURES.register(modBus);

        NetworkDispatcher.init();
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

    public static ResourceLocation location(String location) {
        return new ResourceLocation(RConstants.MODID, location);
    }

    private void doCommonStuff(final FMLCommonSetupEvent event) {
        ActingForwarder.init();
        DownloadSkinsThread.setup();
        RSoundSchemes.init();
        TriggerManager.init();
        StructurePieces.init();
        PlayerUtil.setupPotions();
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
        generator.addProvider(true, new RegenEnglishLang(generator));
        generator.addProvider(true, new RegenLootTables(generator));
        generator.addProvider(true, new RegenLootModifiers(generator));
        RegenBlockTags blockTags = new RegenBlockTags(generator, existingFileHelper);
        generator.addProvider(true, blockTags);
        generator.addProvider(true, new RegenItemTags(generator, blockTags, existingFileHelper));
        generator.addProvider(true, new RegenRecipes(generator));
        generator.addProvider(true, new RegenBiomeModifiers(generator));
        generator.addProvider(true, new RegenAdvancements(generator));
        generator.addProvider(true, new RegenBiomeTags(generator, existingFileHelper));
    }

}
