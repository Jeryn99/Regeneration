package me.swirtzly.regen.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.Regeneration;
import me.swirtzly.regen.client.RKeybinds;
import me.swirtzly.regen.client.rendering.entity.TimelordRenderer;
import me.swirtzly.regen.client.rendering.entity.WatcherRenderer;
import me.swirtzly.regen.client.rendering.layers.HandLayer;
import me.swirtzly.regen.client.rendering.layers.RenderRegenLayer;
import me.swirtzly.regen.common.item.ElixirItem;
import me.swirtzly.regen.common.objects.RBlocks;
import me.swirtzly.regen.common.objects.REntities;
import me.swirtzly.regen.common.objects.RItems;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.util.sound.MovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static me.swirtzly.regen.common.item.FobWatchItem.getEngrave;
import static me.swirtzly.regen.common.item.FobWatchItem.getOpen;

public class ClientUtil {

    public static void doClientStuff() {
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

        RenderingRegistry.registerEntityRenderingHandler(REntities.TIMELORD.get(), TimelordRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(REntities.WATCHER.get(), WatcherRenderer::new);

        RKeybinds.init();

        ItemModelsProperties.registerProperty(RItems.FOB.get(), new ResourceLocation(RConstants.MODID, "model"), (stack, p_call_2_, p_call_3_) -> {
            boolean isGold = getEngrave(stack);
            boolean isOpen = getOpen(stack);
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


        RenderTypeLookup.setRenderLayer(RBlocks.BIO_CONTAINER.get(), RenderType.getCutoutMipped());

        Minecraft.getInstance().getItemColors().register((stack, color) -> color > 0 ? -1 : ElixirItem.getTrait(stack).getColor(), RItems.ELIXIR.get());
    }

    public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(sound, pitch, volume));
    }

    public static void playSound(Object entity, ResourceLocation soundName, SoundCategory category, boolean repeat, Supplier<Boolean> stopCondition, float volume) {
        Minecraft.getInstance().getSoundHandler().play(new MovingSound(entity, new SoundEvent(soundName), category, repeat, stopCondition, volume));
    }

    public static void createToast(TranslationTextComponent title, TranslationTextComponent subtitle) {
        Minecraft.getInstance().getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, title, subtitle));
    }


    public static PointOfView getPlayerPerspective() {
        return Minecraft.getInstance().gameSettings.getPointOfView();
    }

    public static void setPlayerPerspective(String pointOfView) {
        if (RegenConfig.CLIENT.changePerspective.get()) {
            Minecraft.getInstance().gameSettings.setPointOfView(PointOfView.valueOf(pointOfView));
        }
    }

    public static void drawSplitString(MatrixStack ms, FontRenderer fontRenderer, List<ITextComponent> list, float x, float y, int maxWidth, int color) {
        for (ITextComponent t : list) {
            if (t.getUnformattedComponentText().isEmpty() && t.getSiblings().isEmpty()) {
                y += fontRenderer.FONT_HEIGHT;
            } else {
                for (IReorderingProcessor p : fontRenderer.trimStringToWidth(t, maxWidth)) {
                    fontRenderer.func_238407_a_(ms, p, x, y, color);
                    y += fontRenderer.FONT_HEIGHT;
                }
            }
        }
    }
}
