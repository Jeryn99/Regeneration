package mc.craig.software.regen.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.client.rendering.JarTileRender;
import mc.craig.software.regen.client.rendering.model.RModels;
import mc.craig.software.regen.client.rendering.model.armor.GuardArmorModel;
import mc.craig.software.regen.client.rendering.model.armor.RobesModel;
import mc.craig.software.regen.client.rendering.transitions.*;
import mc.craig.software.regen.client.sound.SoundReverb;
import mc.craig.software.regen.common.item.HandItem;
import mc.craig.software.regen.common.item.SpawnItem;
import mc.craig.software.regen.common.objects.RBlocks;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.objects.RTiles;
import mc.craig.software.regen.common.regen.transitions.TransitionTypeRenderers;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.sound.MovingSound;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
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
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

import static mc.craig.software.regen.common.item.FobWatchItem.getEngrave;
import static mc.craig.software.regen.common.item.FobWatchItem.isOpen;

public class ClientUtil {

    public static HashMap<Item, HumanoidModel<?>> ARMOR_MODELS = new HashMap<>();
    public static HashMap<Item, HumanoidModel<?>> ARMOR_MODELS_STEVE = new HashMap<>();

    public static boolean isAlex(Entity livingEntity) {
        if (livingEntity instanceof AbstractClientPlayer abstractClientPlayerEntity) {
            if (ClientUtil.getPlayerInfo(abstractClientPlayerEntity).skinModel.isEmpty()) {
                return false;
            }

            return Objects.equals(ClientUtil.getPlayerInfo(abstractClientPlayerEntity).skinModel, "slim");
        }
        return false;
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
