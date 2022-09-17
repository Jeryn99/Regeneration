package mc.craig.software.regen.forge.handlers;

import mc.craig.software.regen.client.ArmorModelManager;
import mc.craig.software.regen.client.rendering.JarParticle;
import mc.craig.software.regen.client.rendering.layers.HandLayer;
import mc.craig.software.regen.client.rendering.layers.RenderRegenLayer;
import mc.craig.software.regen.client.rendering.model.RModels;
import mc.craig.software.regen.client.rendering.model.forge.RModelsImpl;
import mc.craig.software.regen.client.sound.SoundReverbListener;
import mc.craig.software.regen.common.item.ChaliceItem;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.objects.RParticles;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBus {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        Minecraft.getInstance().getItemColors().register((itemStack, i) -> ChaliceItem.getTrait(itemStack).getPotionColor(), RItems.GAUNTLET.get());
    }

    @SubscribeEvent
    public static void renderLayers(EntityRenderersEvent.AddLayers addLayers) {
        addLayers.getSkins().forEach(skin -> {
            LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> renderer = addLayers.getSkin(skin);
            renderer.addLayer(new RenderRegenLayer(renderer));
            renderer.addLayer(new HandLayer(renderer));
        });

        Minecraft.getInstance().getEntityRenderDispatcher().renderers.forEach((entityType, entityRenderer) -> {
            if (entityRenderer instanceof HumanoidMobRenderer<?, ?> bipedRenderer) {
                bipedRenderer.addLayer(new RenderRegenLayer(bipedRenderer));
                bipedRenderer.addLayer(new HandLayer((RenderLayerParent) entityRenderer));
            }
        });
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.register(RParticles.CONTAINER.get(), JarParticle.Factory::new);
    }

    @SubscribeEvent
    public static void reloadRegisterClient(RegisterClientReloadListenersEvent e) {
        e.registerReloadListener(new SoundReverbListener());
        e.registerReloadListener(new ArmorModelManager());
    }

    @SubscribeEvent
    public static void event(EntityRenderersEvent.RegisterLayerDefinitions event) {
        RModels.init();
        RModelsImpl.register(event);
    }
}
