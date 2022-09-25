package me.craig.software.regen.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.matrix.MatrixStack;
import me.craig.software.regen.client.RKeybinds;
import me.craig.software.regen.client.rendering.JarParticle;
import me.craig.software.regen.client.rendering.JarTileRender;
import me.craig.software.regen.client.rendering.entity.RenderLaser;
import me.craig.software.regen.client.rendering.entity.TimelordRenderer;
import me.craig.software.regen.client.rendering.entity.WatcherRenderer;
import me.craig.software.regen.client.rendering.layers.HandLayer;
import me.craig.software.regen.client.rendering.layers.RenderRegenLayer;
import me.craig.software.regen.client.rendering.model.armor.GuardModel;
import me.craig.software.regen.client.rendering.model.armor.RobesModel;
import me.craig.software.regen.client.rendering.transitions.*;
import me.craig.software.regen.common.objects.*;
import me.craig.software.regen.client.sound.SoundReverb;
import me.craig.software.regen.common.item.ElixirItem;
import me.craig.software.regen.common.item.HandItem;
import me.craig.software.regen.common.item.SpawnItem;
import me.craig.software.regen.common.regen.transitions.TransitionTypeRenderers;
import me.craig.software.regen.common.regen.transitions.TransitionTypes;
import me.craig.software.regen.config.RegenConfig;
import me.craig.software.regen.util.sound.MovingSound;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.RegenPrefTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static me.craig.software.regen.common.item.FobWatchItem.getEngrave;
import static me.craig.software.regen.common.item.FobWatchItem.isOpen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientUtil {

    private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
    public static HashMap<Item, BipedModel<?>> ARMOR_MODELS = new HashMap<>();

    public static String getImgurLink(String base64Image) throws Exception {
        URL url;
        url = new URL("https://who-craft.com/api/index.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String data = URLEncoder.encode("image", "UTF-8") + "="
                + URLEncoder.encode(base64Image, "UTF-8");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();


        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        wr.close();
        rd.close();

        JsonElement jelement = new JsonParser().parse(stb.toString());
        JsonObject jobject = jelement.getAsJsonObject();

        if (JSONUtils.isValidNode(jobject, "link")) {
            return JSONUtils.getAsString(jobject, "link");
        } else {
            throw new Exception(JSONUtils.getAsString(jobject, "message"));
        }
    }

    @SubscribeEvent
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(RParticles.CONTAINER.get(), JarParticle.Factory::new);
    }

    public static boolean isAlex(Entity livingEntity) {
        if (livingEntity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity) livingEntity;
            if (abstractClientPlayerEntity.playerInfo == null || abstractClientPlayerEntity.playerInfo.skinModel == null)
                return false;

            if (abstractClientPlayerEntity.playerInfo.skinModel.isEmpty()) {
                return false;
            }

            return Objects.equals(abstractClientPlayerEntity.playerInfo.skinModel, "slim");
        }
        return false;
    }

    public static void clothingModels() {
        RobesModel robesHead = new RobesModel(EquipmentSlotType.HEAD);
        RobesModel robesChest = new RobesModel(EquipmentSlotType.CHEST);
        RobesModel robesLegs = new RobesModel(EquipmentSlotType.LEGS);
        RobesModel robesFeet = new RobesModel(EquipmentSlotType.FEET);

        //Robes
        ARMOR_MODELS.put(RItems.F_ROBES_HEAD.get(), robesHead);
        ARMOR_MODELS.put(RItems.M_ROBES_HEAD.get(), robesHead);
        ARMOR_MODELS.put(RItems.F_ROBES_CHEST.get(), robesChest);
        ARMOR_MODELS.put(RItems.M_ROBES_CHEST.get(), robesChest);
        ARMOR_MODELS.put(RItems.F_ROBES_LEGS.get(), robesLegs);
        ARMOR_MODELS.put(RItems.M_ROBES_LEGS.get(), robesLegs);
        ARMOR_MODELS.put(RItems.ROBES_FEET.get(), robesFeet);

        BipedModel<?> guardHead = new GuardModel(EquipmentSlotType.HEAD);
        BipedModel<?> guardChest = new GuardModel(EquipmentSlotType.CHEST);
        BipedModel<?> guardLegs = new GuardModel(EquipmentSlotType.LEGS);
        BipedModel<?> guardFeet = new GuardModel(EquipmentSlotType.FEET);

        //Guard
        ARMOR_MODELS.put(RItems.GUARD_HELMET.get(), guardHead);
        ARMOR_MODELS.put(RItems.GUARD_CHEST.get(), guardChest);
        ARMOR_MODELS.put(RItems.GUARD_LEGS.get(), guardLegs);
        ARMOR_MODELS.put(RItems.GUARD_FEET.get(), guardFeet);

    }

    public static BipedModel<?> getArmorModel(ItemStack itemStack) {
        if (!ARMOR_MODELS.containsKey(itemStack.getItem())) {
            throw new UnsupportedOperationException("No model registered for: " + itemStack.getItem());
        }
        return ARMOR_MODELS.get(itemStack.getItem());
    }

    public static void doClientStuff() {
        renderLayers();
        clientRenders();
        itemPredicates();
        setupTabs();
        clothingModels();
        transitionTypes();
        RKeybinds.init();
        TabRegistry.registerEventListeners(MinecraftForge.EVENT_BUS);
        RenderTypeLookup.setRenderLayer(RBlocks.BIO_CONTAINER.get(), RenderType.cutoutMipped());
        Minecraft.getInstance().getItemColors().register((stack, color) -> color > 0 ? -1 : ElixirItem.getTrait(stack).color(), RItems.ELIXIR.get());
    }

    private static void transitionTypes() {
        TransitionTypeRenderers.add(TransitionTypes.FIERY.get(), FieryTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.MINECRAFT_FLAME.get(), MinecraftFlameTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.TROUGHTON.get(), TroughtonTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.WATCHER.get(), WatcherTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.SPARKLE.get(), SparkleTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.BLAZE.get(), BlazeTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.ENDER_DRAGON.get(), EnderDragonTransitionRenderer.INSTANCE);
    }

    private static void renderLayers() {
        /* Attach RenderLayers to Renderers */
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
        for (PlayerRenderer renderPlayer : skinMap.values()) {
            renderPlayer.addLayer(new HandLayer(renderPlayer));
            renderPlayer.addLayer(new RenderRegenLayer(renderPlayer));
        }

        Minecraft.getInstance().getEntityRenderDispatcher().renderers.forEach((entityType, entityRenderer) -> {
            if (entityRenderer instanceof BipedRenderer) {
                BipedRenderer<?, ?> bipedRenderer = (BipedRenderer<?, ?>) entityRenderer;
                bipedRenderer.addLayer(new RenderRegenLayer(bipedRenderer));
                bipedRenderer.addLayer(new HandLayer((IEntityRenderer) entityRenderer));
            }
        });
    }

    private static void setupTabs() {
        MinecraftForge.EVENT_BUS.register(new TabRegistry());

        if (TabRegistry.getTabList().size() < 2) {
            TabRegistry.registerTab(new InventoryTabVanilla());
        }
        TabRegistry.registerTab(new RegenPrefTab());
    }

    private static void itemPredicates() {
        ItemModelsProperties.register(RItems.FOB.get(), new ResourceLocation(RConstants.MODID, "model"), (stack, p_call_2_, p_call_3_) -> {
            boolean isGold = getEngrave(stack);
            boolean isOpen = isOpen(stack);
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

        ItemModelsProperties.register(RItems.RIFLE.get(), new ResourceLocation(RConstants.MODID, "aim"), (stack, p_call_2_, livingEntity) -> {
            if (livingEntity == null) {
                return 0;
            }
            return livingEntity.getUseItemRemainingTicks() > 0 ? 1 : 0;
        });

        ItemModelsProperties.register(RItems.PISTOL.get(), new ResourceLocation(RConstants.MODID, "aim"), (stack, p_call_2_, livingEntity) -> {
            if (livingEntity == null) {
                return 0;
            }
            return livingEntity.getUseItemRemainingTicks() > 0 ? 1 : 0;
        });

        ItemModelsProperties.register(RItems.HAND.get(), new ResourceLocation(RConstants.MODID, "skin_type"), (stack, p_call_2_, livingEntity) -> HandItem.isAlex(stack) ? 1 : 0);


        ItemModelsProperties.register(RItems.SPAWN_ITEM.get(), new ResourceLocation(RConstants.MODID, "timelord"), (itemStack, clientWorld, livingEntity) -> {
            if (itemStack == null || itemStack.isEmpty()) {
                return 0;
            }
            SpawnItem.Timelord type = SpawnItem.getType(itemStack);
            return type.ordinal();
        });

        SoundReverb.addReloader();
    }

    private static void clientRenders() {
        ClientRegistry.bindTileEntityRenderer(RTiles.HAND_JAR.get(), JarTileRender::new);
        RenderingRegistry.registerEntityRenderingHandler(REntities.TIMELORD.get(), TimelordRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(REntities.LASER.get(), RenderLaser::new);
        RenderingRegistry.registerEntityRenderingHandler(REntities.WATCHER.get(), WatcherRenderer::new);
    }

    public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
        Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(sound, pitch, volume));
    }

    public static void playSound(Object entity, ResourceLocation soundName, SoundCategory category, boolean repeat, Supplier<Boolean> stopCondition, float volume) {
        Minecraft.getInstance().getSoundManager().play(new MovingSound(entity, new SoundEvent(soundName), category, repeat, stopCondition, volume));
    }

    public static void createToast(TranslationTextComponent title, TranslationTextComponent subtitle) {
        Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.Type.TUTORIAL_HINT, title, subtitle));
    }

    public static void setPlayerPerspective(String pointOfView) {
        if (RegenConfig.CLIENT.changePerspective.get()) {
            Minecraft.getInstance().options.setCameraType(PointOfView.valueOf(pointOfView));
        }
    }

    public static void renderSky(MatrixStack matrixStackIn) {
        if (Minecraft.getInstance().level == null || matrixStackIn == null) return;
        if (Minecraft.getInstance().level.dimension() != null && Minecraft.getInstance().level.dimension().location().toString().equalsIgnoreCase("regen:gallifrey")) {
            float scale = 30.0F;
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
            matrixStackIn.pushPose();
            matrixStackIn.scale(5, 5, 5);
            matrixStackIn.translate(22, 22, -22);
            Matrix4f matrix4f1 = matrixStackIn.last().pose();
            Minecraft.getInstance().getTextureManager().bind(SUN_TEXTURES);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.vertex(matrix4f1, -scale, 100.0F, -scale).uv(0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, scale, 100.0F, -scale).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, scale, 100.0F, scale).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, -scale, 100.0F, scale).uv(0.0F, 1.0F).endVertex();
            matrixStackIn.popPose();
            bufferbuilder.end();
            WorldVertexBufferUploader.end(bufferbuilder);
        }
    }
}
