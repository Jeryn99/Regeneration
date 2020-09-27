package me.swirtzly.regen;

import me.swirtzly.regen.client.rendering.layers.HandGlowLayer;
import me.swirtzly.regen.client.rendering.layers.RenderFieryArms;
import me.swirtzly.regen.common.cap.IRegen;
import me.swirtzly.regen.common.cap.RegenCap;
import me.swirtzly.regen.common.cap.RegenStorage;
import me.swirtzly.regen.data.EnglishLang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@Mod("regen")
public class Regeneration {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Regeneration() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doCommonStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }


    private void doCommonStuff(final FMLCommonSetupEvent event){
        CapabilityManager.INSTANCE.register(IRegen.class, new RegenStorage(), RegenCap::new);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
        for (PlayerRenderer renderPlayer : skinMap.values()) {
            renderPlayer.addLayer(new HandGlowLayer(renderPlayer));
            renderPlayer.addLayer(new RenderFieryArms(renderPlayer));
        }
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent e) {
        DataGenerator generator = e.getGenerator();
        generator.addProvider(new EnglishLang(generator));
    }

}
