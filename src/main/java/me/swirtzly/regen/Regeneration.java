package me.swirtzly.regen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.swirtzly.regen.client.rendering.entity.ItemOverrideRenderer;
import me.swirtzly.regen.client.rendering.entity.TimelordRenderer;
import me.swirtzly.regen.client.rendering.layers.HandLayer;
import me.swirtzly.regen.client.rendering.layers.RenderRegenLayer;
import me.swirtzly.regen.client.skin.CommonSkin;
import me.swirtzly.regen.common.entities.TimelordEntity;
import me.swirtzly.regen.common.item.FobWatchItem;
import me.swirtzly.regen.common.objects.REntities;
import me.swirtzly.regen.common.objects.RItems;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.RegenStorage;
import me.swirtzly.regen.common.regen.acting.ActingForwarder;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.data.EnglishLang;
import me.swirtzly.regen.data.RRecipe;
import me.swirtzly.regen.data.RSoundsGen;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.util.PlayerUtil;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static me.swirtzly.regen.common.item.FobWatchItem.getEngrave;
import static me.swirtzly.regen.common.item.FobWatchItem.getOpen;

@Mod("regen")
public class Regeneration {

    public static final Logger LOG = LogManager.getLogger("Regeneration");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public Regeneration() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doCommonStuff);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        NetworkDispatcher.setUp();
        PlayerUtil.setupPotions();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);
    }


    private void doCommonStuff(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IRegen.class, new RegenStorage(), RegenCap::new);
        ActingForwarder.init();
        GlobalEntityTypeAttributes.put(REntities.TIMELORD.get(), TimelordEntity.createAttributes().create());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        /* Attach RenderLayers to Renderers */
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
        for (PlayerRenderer renderPlayer : skinMap.values()) {
            renderPlayer.addLayer(new HandLayer(renderPlayer));
            renderPlayer.addLayer(new RenderRegenLayer(renderPlayer));
        }

        Minecraft.getInstance().getRenderManager().renderers.forEach((entityType, entityRenderer) -> {
            if (entityRenderer instanceof BipedRenderer) {
                ((BipedRenderer<?, ?>) entityRenderer).addLayer(new RenderRegenLayer((IEntityRenderer) entityRenderer));
                ((BipedRenderer<?, ?>) entityRenderer).addLayer(new HandLayer((IEntityRenderer) entityRenderer));
            }
        });


        CommonSkin.doSetupOnThread();

        RenderingRegistry.registerEntityRenderingHandler(REntities.ITEM_OVERRIDE_ENTITY_TYPE.get(), ItemOverrideRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(REntities.TIMELORD.get(), TimelordRenderer::new);

        ItemModelsProperties.registerProperty(RItems.FOB.get(), new ResourceLocation(RConstants.MODID, "is_open"), (stack, p_call_2_, p_call_3_) -> {
            if (FobWatchItem.getStackTag(stack) == null || !FobWatchItem.getStackTag(stack).contains("is_open")) {
                return 0F; // Closed
            }
            return getOpen(stack);
        });

        ItemModelsProperties.registerProperty(RItems.FOB.get(), new ResourceLocation(RConstants.MODID, "is_gold"), (stack, p_call_2_, p_call_3_) -> {
            if (FobWatchItem.getStackTag(stack) == null || !FobWatchItem.getStackTag(stack).contains("is_gold")) {
                return 0F; // Closed
            }
            return getEngrave(stack);
        });
    }

    @SubscribeEvent
    public void onGatherData(GatherDataEvent e) {
        DataGenerator generator = e.getGenerator();
        generator.addProvider(new EnglishLang(generator));
        generator.addProvider(new RSoundsGen(generator));
        generator.addProvider(new RRecipe(generator));
    }

    @SubscribeEvent
    public void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        RSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        REntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
