package mc.craig.software.regen.util;

<<<<<<< HEAD
import com.mojang.authlib.minecraft.client.MinecraftClient;
=======
>>>>>>> arch-take-two
import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.client.RKeybinds;
import mc.craig.software.regen.client.rendering.JarTileRender;
import mc.craig.software.regen.client.rendering.entity.TimelordRenderer;
import mc.craig.software.regen.client.rendering.model.RModels;
import mc.craig.software.regen.client.rendering.model.armor.GuardArmorModel;
import mc.craig.software.regen.client.rendering.model.armor.RobesModel;
import mc.craig.software.regen.client.rendering.transitions.*;
import mc.craig.software.regen.client.skin.VisualManipulator;
<<<<<<< HEAD
import mc.craig.software.regen.common.item.ChaliceItem;
=======
>>>>>>> arch-take-two
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.objects.RSounds;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionTypeRenderers;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.sound.MovingSound;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.model.HumanoidModel;
<<<<<<< HEAD
=======
import net.minecraft.client.model.PlayerModel;
>>>>>>> arch-take-two
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

public class ClientUtil {

    public static HashMap<Item, HumanoidModel<?>> ARMOR_MODELS = new HashMap<>();
    public static HashMap<Item, HumanoidModel<?>> ARMOR_MODELS_STEVE = new HashMap<>();
    private static SimpleSoundInstance humAmbienceSound;

    public static void handleInput(LocalPlayer localPlayer, Input input) {
        RegenerationData.get(localPlayer).ifPresent((data -> {
            if (data.regenState() == RegenStates.REGENERATING) { // locking user
                blockMovement(input);
                upwardsMovement(localPlayer, data);
            }
        }));
    }

    private static void upwardsMovement(LocalPlayer player, IRegen data) {
        if (data.transitionType() == TransitionTypes.ENDER_DRAGON && RegenConfig.COMMON.allowUpwardsMotion.get()) {
            if (player.blockPosition().getY() <= 100) {
                BlockPos upwards = player.blockPosition().above(2);
                BlockPos pos = upwards.subtract(player.blockPosition());
                Vec3 vec = new Vec3(pos.getX(), pos.getY(), pos.getZ()).normalize();
                player.setDeltaMovement(player.getDeltaMovement().add(vec.scale(0.10D)));
            }
        }
    }

    public static void getHandToHide(PlayerModel<?> playerModel, AbstractClientPlayer abstractClientPlayer) {

    }

    private static void blockMovement(Input moveType) {
        moveType.right = false;
        moveType.left = false;
        moveType.down = false;
        moveType.jumping = false;
        moveType.forwardImpulse = 0.0F;
        moveType.shiftKeyDown = false;
        moveType.leftImpulse = 0.0F;
    }

    public static boolean isAlex(Entity livingEntity) {
        if (livingEntity instanceof AbstractClientPlayer abstractClientPlayerEntity) {

            if (ClientUtil.getPlayerInfo(abstractClientPlayerEntity) == null || ClientUtil.getPlayerInfo(abstractClientPlayerEntity).skinModel == null)
                return false;

            if (ClientUtil.getPlayerInfo(abstractClientPlayerEntity).skinModel.isEmpty()) {
                return false;
            }

            return Objects.equals(ClientUtil.getPlayerInfo(abstractClientPlayerEntity).skinModel, "slim");
        }
        return false;
    }

    public static void tickClient() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer ep = Minecraft.getInstance().player;
            SoundManager sound = Minecraft.getInstance().getSoundManager();
            RegenerationData.get(ep).ifPresent(iRegen -> {
                if (iRegen.regenState() == RegenStates.POST && PlayerUtil.isPlayerAboveZeroGrid(ep)) {

                    if (humAmbienceSound == null) {
                        humAmbienceSound = SimpleSoundInstance.forLocalAmbience(RSounds.GRACE_HUM.get(), 1, 1);
                    }

                    if (!sound.isActive(humAmbienceSound)) {
                        sound.play(humAmbienceSound);
                    }
                } else {
                    if (sound.isActive(humAmbienceSound)) {
                        sound.stop(humAmbienceSound);
                    }
                }
            });
        }

        RKeybinds.tickKeybinds();
        destroyTextures();
    }

    public static void destroyTextures() {
        //Clean up our mess we might have made!
        if (Minecraft.getInstance().level == null) {
            if (VisualManipulator.PLAYER_SKINS.size() > 0) {
                VisualManipulator.PLAYER_SKINS.forEach(((uuid, texture) -> Minecraft.getInstance().getTextureManager().release(texture)));
                VisualManipulator.PLAYER_SKINS.clear();
                TimelordRenderer.TIMELORDS.forEach(((uuid, texture) -> Minecraft.getInstance().getTextureManager().release(texture)));
                TimelordRenderer.TIMELORDS.clear();
                JarTileRender.TEXTURES.forEach(((uuid, texture) -> Minecraft.getInstance().getTextureManager().release(texture)));
                JarTileRender.TEXTURES.clear();
                Regeneration.LOGGER.warn("Cleared Regeneration texture cache");
            }
        }
    }

    public static void clothingModels() {

        if (!ARMOR_MODELS.isEmpty()) return;

        ModelPart bakedGuard = Minecraft.getInstance().getEntityModels().bakeLayer(RModels.GUARD_ARMOR);
        GuardArmorModel guardHead = new GuardArmorModel(bakedGuard, EquipmentSlot.HEAD);
        GuardArmorModel guardChest = new GuardArmorModel(bakedGuard, EquipmentSlot.CHEST);
        GuardArmorModel guardLegs = new GuardArmorModel(bakedGuard, EquipmentSlot.LEGS);
        GuardArmorModel guardFeet = new GuardArmorModel(bakedGuard, EquipmentSlot.FEET);

        ModelPart bakedRobes = Minecraft.getInstance().getEntityModels().bakeLayer(RModels.COUNCIL_ROBES);
        RobesModel robesHead = new RobesModel(bakedRobes, EquipmentSlot.HEAD);
        RobesModel robesChest = new RobesModel(bakedRobes, EquipmentSlot.CHEST);
        RobesModel robesLegs = new RobesModel(bakedRobes, EquipmentSlot.LEGS);
        RobesModel robesFeet = new RobesModel(bakedRobes, EquipmentSlot.FEET);

        ModelPart bakedRobesSteve = Minecraft.getInstance().getEntityModels().bakeLayer(RModels.COUNCIL_ROBES_STEVE);
        RobesModel robesChestSteve = new RobesModel(bakedRobesSteve, EquipmentSlot.CHEST);

        ModelPart bakedGuardSteve = Minecraft.getInstance().getEntityModels().bakeLayer(RModels.GUARD_ARMOR_STEVE);
        GuardArmorModel armorSteve = new GuardArmorModel(bakedGuardSteve, EquipmentSlot.CHEST);

        //Robes
        ARMOR_MODELS_STEVE.put(RItems.F_ROBES_CHEST.get(), robesChestSteve);
        ARMOR_MODELS_STEVE.put(RItems.M_ROBES_CHEST.get(), robesChestSteve);

        ARMOR_MODELS.put(RItems.F_ROBES_HEAD.get(), robesHead);
        ARMOR_MODELS.put(RItems.M_ROBES_HEAD.get(), robesHead);
        ARMOR_MODELS.put(RItems.F_ROBES_CHEST.get(), robesChest);
        ARMOR_MODELS.put(RItems.M_ROBES_CHEST.get(), robesChest);
        ARMOR_MODELS.put(RItems.F_ROBES_LEGS.get(), robesLegs);
        ARMOR_MODELS.put(RItems.M_ROBES_LEGS.get(), robesLegs);
        ARMOR_MODELS.put(RItems.ROBES_FEET.get(), robesFeet);

        //Guard
        ARMOR_MODELS_STEVE.put(RItems.GUARD_CHEST.get(), armorSteve);
        ARMOR_MODELS.put(RItems.GUARD_HELMET.get(), guardHead);
        ARMOR_MODELS.put(RItems.GUARD_CHEST.get(), guardChest);
        ARMOR_MODELS.put(RItems.GUARD_LEGS.get(), guardLegs);
        ARMOR_MODELS.put(RItems.GUARD_FEET.get(), guardFeet);

    }

    public static HumanoidModel<?> getArmorModel(ItemStack itemStack, LivingEntity livingEntity) {

        if (livingEntity instanceof AbstractClientPlayer player) {
            boolean isSlim = (Objects.equals(ClientUtil.getPlayerInfo(player).skinModel, "slim"));
            if (isSlim) {
                return getHumanoidModel(itemStack, false);
            }
        }
        return getHumanoidModel(itemStack, true);
    }


    private static HumanoidModel<?> getHumanoidModel(ItemStack itemStack, boolean trySteve) {

        if (trySteve) {
            if (ARMOR_MODELS_STEVE.containsKey(itemStack.getItem())) {
                return ARMOR_MODELS_STEVE.get(itemStack.getItem());
            }
        }

        if (!ARMOR_MODELS.containsKey(itemStack.getItem())) {
            throw new UnsupportedOperationException("No model registered for: " + itemStack.getItem());
        }
        return ARMOR_MODELS.get(itemStack.getItem());
    }

    public static void doClientStuff() {
        itemPredicates();
        transitionTypes();
        renderers();
    }

    @ExpectPlatform
    public static void renderers(){
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void itemPredicates(){
        throw new AssertionError();
    }

    private static void transitionTypes() {
        TransitionTypeRenderers.add(TransitionTypes.FIERY, FieryTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.TRISTIS_IGNIS, SadFieryTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.TROUGHTON, TroughtonTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.WATCHER, WatcherTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.SPARKLE, SparkleTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.BLAZE, BlazeTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.ENDER_DRAGON, EnderDragonTransitionRenderer.INSTANCE);
        TransitionTypeRenderers.add(TransitionTypes.SNEEZE, SneezeTransitionRenderer.INSTANCE);
    }

    public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, pitch, volume));
    }

    public static void playSound(Object entity, ResourceLocation soundName, SoundSource category, boolean repeat, Supplier<Boolean> stopCondition, float volume, RandomSource randomSource) {
        Minecraft.getInstance().getSoundManager().play(new MovingSound(entity, new SoundEvent(soundName), category, repeat, stopCondition, volume, randomSource));
    }

    public static void createToast(MutableComponent title, MutableComponent subtitle) {
        Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT, title, subtitle));
    }

    public static void setPlayerPerspective(String pointOfView) {
        if (RegenConfig.CLIENT.changePerspective.get()) {
            Minecraft.getInstance().options.setCameraType(CameraType.valueOf(pointOfView));
        }
    }

    public static PlayerInfo getPlayerInfo(AbstractClientPlayer player) {
        return player.playerInfo;
    }
}
