package me.suff.mc.regen.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.suff.mc.regen.client.RKeybinds;
import me.suff.mc.regen.client.rendering.JarTileRender;
import me.suff.mc.regen.client.rendering.entity.RenderLaser;
import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.client.rendering.entity.WatcherRenderer;
import me.suff.mc.regen.client.rendering.layers.HandLayer;
import me.suff.mc.regen.client.rendering.layers.RenderRegenLayer;
import me.suff.mc.regen.client.rendering.model.armor.GuardModel;
import me.suff.mc.regen.client.rendering.model.armor.RobesModel;
import me.suff.mc.regen.client.sound.SoundReverb;
import me.suff.mc.regen.common.item.ElixirItem;
import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.item.SpawnItem;
import me.suff.mc.regen.common.objects.RBlocks;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RTiles;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.sound.MovingSound;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.RegenPrefTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static me.suff.mc.regen.common.item.FobWatchItem.getEngrave;
import static me.suff.mc.regen.common.item.FobWatchItem.getOpen;

public class ClientUtil {

    private static final BipedModel< ? > GUARD_HEAD = new GuardModel(EquipmentSlotType.HEAD);
    private static final BipedModel< ? > GUARD_BODY = new GuardModel(EquipmentSlotType.CHEST);
    private static final BipedModel< ? > GUARD_LEGS = new GuardModel(EquipmentSlotType.LEGS);
    private static final BipedModel< ? > GUARD_FEET = new GuardModel(EquipmentSlotType.FEET);

    private static final BipedModel< ? > ROBES_HEAD = new RobesModel(EquipmentSlotType.HEAD);
    private static final BipedModel< ? > ROBES_BODY = new RobesModel(EquipmentSlotType.CHEST);
    private static final BipedModel< ? > ROBES_LEGS = new RobesModel(EquipmentSlotType.LEGS);
    private static final BipedModel< ? > ROBES_FEET = new RobesModel(EquipmentSlotType.FEET);
    private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");

    //TODO maybe I should make this a hashmap
    public static BipedModel< ? > getArmorModel(ItemStack itemStack) {

        if (itemStack.getItem() == RItems.F_ROBES_HEAD.get() || itemStack.getItem() == RItems.M_ROBES_HEAD.get()) {
            return ROBES_HEAD;
        }

        if (itemStack.getItem() == RItems.F_ROBES_CHEST.get() || itemStack.getItem() == RItems.M_ROBES_CHEST.get()) {
            return ROBES_BODY;
        }

        if (itemStack.getItem() == RItems.F_ROBES_LEGS.get() || itemStack.getItem() == RItems.M_ROBES_LEGS.get()) {
            return ROBES_LEGS;
        }

        if (itemStack.getItem() == RItems.ROBES_FEET.get()) {
            return ROBES_FEET;
        }

        if (itemStack.getItem() == RItems.GUARD_HELMET.get()) {
            return GUARD_HEAD;
        }

        if (itemStack.getItem() == RItems.GUARD_CHEST.get()) {
            return GUARD_BODY;
        }

        if (itemStack.getItem() == RItems.GUARD_LEGS.get()) {
            return GUARD_LEGS;
        }

        if (itemStack.getItem() == RItems.GUARD_FEET.get()) {
            return GUARD_FEET;
        }
        return GUARD_FEET;
    }

    public static void doClientStuff() {
        /* Attach RenderLayers to Renderers */
        Map< String, PlayerRenderer > skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
        for (PlayerRenderer renderPlayer : skinMap.values()) {
            renderPlayer.addLayer(new HandLayer(renderPlayer));
            renderPlayer.addLayer(new RenderRegenLayer(renderPlayer));
        }

        Minecraft.getInstance().getRenderManager().renderers.forEach((entityType, entityRenderer) -> {
            if (entityRenderer instanceof BipedRenderer) {
                ((BipedRenderer< ?, ? >) entityRenderer).addLayer(new RenderRegenLayer((IEntityRenderer) entityRenderer));
                ((BipedRenderer< ?, ? >) entityRenderer).addLayer(new HandLayer((IEntityRenderer) entityRenderer));
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(REntities.TIMELORD.get(), TimelordRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(REntities.LASER.get(), RenderLaser::new);
        RenderingRegistry.registerEntityRenderingHandler(REntities.WATCHER.get(), WatcherRenderer::new);

        ClientRegistry.bindTileEntityRenderer(RTiles.HAND_JAR.get(), JarTileRender::new);

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

        ItemModelsProperties.registerProperty(RItems.RIFLE.get(), new ResourceLocation(RConstants.MODID, "aim"), (stack, p_call_2_, livingEntity) -> {
            if (livingEntity == null) {
                return 0;
            }
            return livingEntity.getItemInUseCount() > 0 ? 1 : 0;
        });

        ItemModelsProperties.registerProperty(RItems.PISTOL.get(), new ResourceLocation(RConstants.MODID, "aim"), (stack, p_call_2_, livingEntity) -> {
            if (livingEntity == null) {
                return 0;
            }
            return livingEntity.getItemInUseCount() > 0 ? 1 : 0;
        });

        ItemModelsProperties.registerProperty(RItems.HAND.get(), new ResourceLocation(RConstants.MODID, "skin_type"), (stack, p_call_2_, livingEntity) -> {
            return HandItem.isAlex(stack) ? 0 : 1;
        });


        ItemModelsProperties.registerProperty(RItems.SPAWN_ITEM.get(), new ResourceLocation(RConstants.MODID, "timelord"), (itemStack, clientWorld, livingEntity) -> {
            if (itemStack == null || itemStack.isEmpty()) {
                return 0;
            }
            SpawnItem.Timelord type = SpawnItem.getType(itemStack);
            return type.ordinal();
        });

        RenderTypeLookup.setRenderLayer(RBlocks.BIO_CONTAINER.get(), RenderType.getCutoutMipped());

        Minecraft.getInstance().getItemColors().register((stack, color) -> color > 0 ? -1 : ElixirItem.getTrait(stack).getColor(), RItems.ELIXIR.get());

        MinecraftForge.EVENT_BUS.register(new TabRegistry());

        if (TabRegistry.getTabList().size() < 2) {
            TabRegistry.registerTab(new InventoryTabVanilla());
        }
        TabRegistry.registerTab(new RegenPrefTab());
    }

    public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(sound, pitch, volume));
    }

    public static void playSound(Object entity, ResourceLocation soundName, SoundCategory category, boolean repeat, Supplier< Boolean > stopCondition, float volume) {
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

    public static void drawSplitString(MatrixStack ms, FontRenderer fontRenderer, List< ITextComponent > list, float x, float y, int maxWidth, int color) {
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

    public static void renderSky(MatrixStack matrixStackIn, float partialTicks) {
        if (Minecraft.getInstance().world.getDimensionKey().getLocation().getPath().contains("gallifrey")) {
                float scale = 30.0F;
                BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
                matrixStackIn.push();
                matrixStackIn.scale(5, 5, 5);
                Matrix4f matrix4f1 = matrixStackIn.getLast().getMatrix();
                Minecraft.getInstance().getTextureManager().bindTexture(SUN_TEXTURES);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                bufferbuilder.pos(matrix4f1, -scale, 100.0F, -scale).tex(0.0F, 0.0F).endVertex();
                bufferbuilder.pos(matrix4f1, scale, 100.0F, -scale).tex(1.0F, 0.0F).endVertex();
                bufferbuilder.pos(matrix4f1, scale, 100.0F, scale).tex(1.0F, 1.0F).endVertex();
                bufferbuilder.pos(matrix4f1, -scale, 100.0F, scale).tex(0.0F, 1.0F).endVertex();
                matrixStackIn.pop();
                bufferbuilder.finishDrawing();
                WorldVertexBufferUploader.draw(bufferbuilder);
        }
    }
}
