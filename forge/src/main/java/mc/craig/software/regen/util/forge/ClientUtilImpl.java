package mc.craig.software.regen.util.forge;

import mc.craig.software.regen.client.rendering.JarTileRender;
import mc.craig.software.regen.client.rendering.entity.RenderLaser;
import mc.craig.software.regen.client.rendering.entity.TimelordRenderer;
import mc.craig.software.regen.client.rendering.entity.WatcherRenderer;
import mc.craig.software.regen.client.sound.SoundReverb;
import mc.craig.software.regen.common.item.HandItem;
import mc.craig.software.regen.common.item.SpawnItem;
import mc.craig.software.regen.common.objects.RBlocks;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.objects.RTiles;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

import static mc.craig.software.regen.common.item.FobWatchItem.getEngrave;
import static mc.craig.software.regen.common.item.FobWatchItem.isOpen;

public class ClientUtilImpl {

    public static void itemPredicates() {
        ItemProperties.register(RItems.FOB.get(), new ResourceLocation(RConstants.MODID, "model"), (ClampedItemPropertyFunction) (itemStack, clientLevel, livingEntity, i) -> {
            boolean isGold = getEngrave(itemStack);
            boolean isOpen = isOpen(itemStack);
            if (isOpen && isGold) {
                return 0.2F;
            }

            if (!isOpen && !isGold) {
                return 0.3F;
            }

            if (isOpen) {
                return 0.4F;
            }


            return 0.1F;
        });

        ItemProperties.register(RItems.RIFLE.get(), new ResourceLocation(RConstants.MODID, "aim"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0;
            }
            return livingEntity.getUseItemRemainingTicks() > 0 ? 1 : 0;
        });

        ItemProperties.register(RItems.PISTOL.get(), new ResourceLocation(RConstants.MODID, "aim"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0;
            }
            return livingEntity.getUseItemRemainingTicks() > 0 ? 1 : 0;
        });

        ItemProperties.register(RItems.HAND.get(), new ResourceLocation(RConstants.MODID, "skin_type"), (itemStack, clientLevel, livingEntity, i) -> HandItem.isAlex(itemStack) ? 1 : 0);


        ItemProperties.register(RItems.SPAWN_ITEM.get(), new ResourceLocation(RConstants.MODID, "timelord"), (itemStack, clientWorld, livingEntity, something) -> {
            if (itemStack == null || itemStack.isEmpty()) {
                return 0;
            }
            SpawnItem.Timelord type = SpawnItem.getType(itemStack);
            return type.ordinal();
        });

        SoundReverb.addReloader();
    }

    public static void renderers() {
        BlockEntityRenderers.register(RTiles.HAND_JAR.get(), JarTileRender::new);
        ItemBlockRenderTypes.setRenderLayer(RBlocks.BIO_CONTAINER.get(), RenderType.cutoutMipped());

        EntityRenderers.register(REntities.TIMELORD.get(), TimelordRenderer::new);
        EntityRenderers.register(REntities.LASER.get(), RenderLaser::new);
        EntityRenderers.register(REntities.WATCHER.get(), WatcherRenderer::new);
    }
}
