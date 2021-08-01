package me.suff.mc.regen.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.PoseStack;
import me.suff.mc.regen.client.RKeybinds;
import me.suff.mc.regen.client.rendering.JarParticle;
import me.suff.mc.regen.client.rendering.JarTileRender;
import me.suff.mc.regen.client.rendering.entity.RenderLaser;
import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.client.rendering.entity.WatcherRenderer;
import me.suff.mc.regen.client.rendering.layers.HandLayer;
import me.suff.mc.regen.client.rendering.layers.RenderRegenLayer;
import me.suff.mc.regen.client.rendering.model.armor.GuardModel;
import me.suff.mc.regen.client.rendering.model.armor.RobesModel;
import me.suff.mc.regen.client.rendering.transitions.*;
import me.suff.mc.regen.client.sound.SoundReverb;
import me.suff.mc.regen.common.item.ElixirItem;
import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.item.SpawnItem;
import me.suff.mc.regen.common.objects.*;
import me.suff.mc.regen.common.regen.transitions.TransitionTypeRenderers;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.sound.MovingSound;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.RegenPrefTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

import static me.suff.mc.regen.common.item.FobWatchItem.getEngrave;
import static me.suff.mc.regen.common.item.FobWatchItem.isOpen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientUtil {

    private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
    public static HashMap<Item, HumanoidModel<?>> ARMOR_MODELS = new HashMap<>();

    public static ModelPart getPlayerModel(boolean slim) {
        return Minecraft.getInstance().getEntityModels().bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER);
    }

    public static String getImgurLink(String base64Image) throws Exception {
        URL url;
        url = new URL("https://who-craft.com/api/index.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String data = URLEncoder.encode("image", StandardCharsets.UTF_8) + "="
                + URLEncoder.encode(base64Image, StandardCharsets.UTF_8);

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

        if (GsonHelper.isValidNode(jobject, "link")) {
            return GsonHelper.getAsString(jobject, "link");
        } else {
            throw new Exception(GsonHelper.getAsString(jobject, "message"));
        }
    }


    @SubscribeEvent
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(RParticles.CONTAINER.get(), JarParticle.Factory::new);
    }

    public static boolean isAlex(Entity livingEntity) {
        if (livingEntity instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractClientPlayerEntity = (AbstractClientPlayer) livingEntity;
            if (abstractClientPlayerEntity.playerInfo.skinModel.isEmpty()) {
                return false;
            }

            return Objects.equals(abstractClientPlayerEntity.playerInfo.skinModel, "slim");
        }
        return false;
    }

    public static void clothingModels() {
        RobesModel robesHead = new RobesModel(EquipmentSlot.HEAD);
        RobesModel robesChest = new RobesModel(EquipmentSlot.CHEST);
        RobesModel robesLegs = new RobesModel(EquipmentSlot.LEGS);
        RobesModel robesFeet = new RobesModel(EquipmentSlot.FEET);

        //Robes
        ARMOR_MODELS.put(RItems.F_ROBES_HEAD.get(), robesHead);
        ARMOR_MODELS.put(RItems.M_ROBES_HEAD.get(), robesHead);
        ARMOR_MODELS.put(RItems.F_ROBES_CHEST.get(), robesChest);
        ARMOR_MODELS.put(RItems.M_ROBES_CHEST.get(), robesChest);
        ARMOR_MODELS.put(RItems.F_ROBES_LEGS.get(), robesLegs);
        ARMOR_MODELS.put(RItems.M_ROBES_LEGS.get(), robesLegs);
        ARMOR_MODELS.put(RItems.ROBES_FEET.get(), robesFeet);

        HumanoidModel<?> guardHead = new GuardModel(EquipmentSlot.HEAD);
        HumanoidModel<?> guardChest = new GuardModel(EquipmentSlot.CHEST);
        HumanoidModel<?> guardLegs = new GuardModel(EquipmentSlot.LEGS);
        HumanoidModel<?> guardFeet = new GuardModel(EquipmentSlot.FEET);

        //Guard
        ARMOR_MODELS.put(RItems.GUARD_HELMET.get(), guardHead);
        ARMOR_MODELS.put(RItems.GUARD_CHEST.get(), guardChest);
        ARMOR_MODELS.put(RItems.GUARD_LEGS.get(), guardLegs);
        ARMOR_MODELS.put(RItems.GUARD_FEET.get(), guardFeet);

    }

    public static HumanoidModel<?> getArmorModel(ItemStack itemStack) {
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
        ItemBlockRenderTypes.setRenderLayer(RBlocks.BIO_CONTAINER.get(), RenderType.cutoutMipped());
        Minecraft.getInstance().getItemColors().register((stack, color) -> color > 0 ? -1 : ElixirItem.getTrait(stack).color(), RItems.ELIXIR.get());
    }

    private static void transitionTypes() {
        TransitionTypeRenderers.add(TransitionTypes.FIERY.get(), FieryTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.TROUGHTON.get(), TroughtonTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.WATCHER.get(), WatcherTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.SPARKLE.get(), SparkleTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.BLAZE.get(), BlazeTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.ENDER_DRAGON.get(), EnderDragonTransitionRenderer.INSTANCE);
    }


    @SubscribeEvent
    public static void renderLayers(EntityRenderersEvent.AddLayers addLayers) {
        addLayers.getSkins().forEach(skin -> {
            LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> renderer = addLayers.getSkin(skin);
            renderer.addLayer(new HandLayer(renderer));
            renderer.addLayer(new RenderRegenLayer(renderer));
        });

        Minecraft.getInstance().getEntityRenderDispatcher().renderers.forEach((entityType, entityRenderer) -> {
            if (entityRenderer instanceof HumanoidMobRenderer) {
                HumanoidMobRenderer<?, ?> bipedRenderer = (HumanoidMobRenderer<?, ?>) entityRenderer;
                bipedRenderer.addLayer(new RenderRegenLayer(bipedRenderer));
                bipedRenderer.addLayer(new HandLayer((RenderLayerParent) entityRenderer));
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
        ItemProperties.register(RItems.FOB.get(), new ResourceLocation(RConstants.MODID, "model"), (stack, p_call_2_, p_call_3_, something) -> {
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

        ItemProperties.register(RItems.RIFLE.get(), new ResourceLocation(RConstants.MODID, "aim"), (stack, p_call_2_, livingEntity, something) -> {
            if (livingEntity == null) {
                return 0;
            }
            return livingEntity.getUseItemRemainingTicks() > 0 ? 1 : 0;
        });

        ItemProperties.register(RItems.PISTOL.get(), new ResourceLocation(RConstants.MODID, "aim"), (stack, p_call_2_, livingEntity, something) -> {
            if (livingEntity == null) {
                return 0;
            }
            return livingEntity.getUseItemRemainingTicks() > 0 ? 1 : 0;
        });

        ItemProperties.register(RItems.HAND.get(), new ResourceLocation(RConstants.MODID, "skin_type"), (stack, p_call_2_, livingEntity, something) -> HandItem.isAlex(stack) ? 1 : 0);


        ItemProperties.register(RItems.SPAWN_ITEM.get(), new ResourceLocation(RConstants.MODID, "timelord"), (itemStack, clientWorld, livingEntity, something) -> {
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
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, pitch, volume));
    }

    public static void playSound(Object entity, ResourceLocation soundName, SoundSource category, boolean repeat, Supplier<Boolean> stopCondition, float volume) {
        Minecraft.getInstance().getSoundManager().play(new MovingSound(entity, new SoundEvent(soundName), category, repeat, stopCondition, volume));
    }

    public static void createToast(TranslatableComponent title, TranslatableComponent subtitle) {
        Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT, title, subtitle));
    }

    public static void setPlayerPerspective(String pointOfView) {
        if (RegenConfig.CLIENT.changePerspective.get()) {
            Minecraft.getInstance().options.setCameraType(CameraType.valueOf(pointOfView));
        }
    }

    public static void renderSky(PoseStack matrixStackIn) {
        if (Minecraft.getInstance().level == null || matrixStackIn == null) return;
      /*  if (Minecraft.getInstance().level.dimension() != null &&  Minecraft.getInstance().level.dimension()== RConstants.GALLIFREY) {
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
        }*/
    }
}
