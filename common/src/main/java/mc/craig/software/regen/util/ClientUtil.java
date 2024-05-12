package mc.craig.software.regen.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.client.RKeybinds;
import mc.craig.software.regen.client.rendering.JarTileRender;
import mc.craig.software.regen.client.rendering.entity.TimelordRenderer;
import mc.craig.software.regen.client.rendering.model.RModels;
import mc.craig.software.regen.client.rendering.model.armor.ArmorModel;
import mc.craig.software.regen.client.rendering.transitions.*;
import mc.craig.software.regen.client.screen.IncarnationScreen;
import mc.craig.software.regen.client.skin.VisualManipulator;
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
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.PlayerSkin;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class ClientUtil {

    public static HashMap<Item, HumanoidModel<?>> ARMOR_MODELS = new HashMap<>();
    public static HashMap<Item, HumanoidModel<?>> ARMOR_MODELS_STEVE = new HashMap<>();
    private static SimpleSoundInstance humAmbienceSound;

    /**
     * Handles the given input for the given local player.
     *
     * @param localPlayer the local player to handle the input for
     * @param input       the input to handle
     */
    public static void handleInput(LocalPlayer localPlayer, Input input) {
        // Get the RegenerationData for the given player, if it exists
        Optional<RegenerationData> regenerationData = RegenerationData.get(localPlayer);
        if (regenerationData.isPresent()) {
            RegenerationData data = regenerationData.get();

            // If the player is currently regenerating, block their movement and
            // handle upwards movement
            if (data.regenState() == RegenStates.REGENERATING) {
                blockMovement(input);
                upwardsMovement(localPlayer, data);
            }
        }
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

    /**
     * Blocks movement for the given input.
     *
     * @param moveType the input to block movement for
     */
    private static void blockMovement(Input moveType) {
        // Set all movement-related fields to false or 0.0F to block movement
        moveType.right = false;
        moveType.left = false;
        moveType.down = false;
        moveType.jumping = false;
        moveType.forwardImpulse = 0.0F;
        moveType.shiftKeyDown = false;
        moveType.leftImpulse = 0.0F;
    }

    /**
     * Checks if the given living entity is the Alex skin type.
     *
     * @param livingEntity the living entity to check
     * @return true if the given living entity is the Alex skin type, false otherwise
     */
    public static boolean isAlex(Entity livingEntity) {
        // Check if the given entity is an AbstractClientPlayer
        if (livingEntity instanceof AbstractClientPlayer abstractClientPlayerEntity) {
            // Get the PlayerInfo for the AbstractClientPlayer
            PlayerInfo playerInfo = ClientUtil.getPlayerInfo(abstractClientPlayerEntity);

            // Return false if the PlayerInfo is null or the skin model is empty
            if (playerInfo == null || playerInfo.getSkin() == null || playerInfo.getSkin().model() == null) {
                return false;
            }

            // Return true if the skin model is "slim" (which corresponds to the Alex skin type)
            return Objects.equals(playerInfo.getSkin(), PlayerSkin.Model.SLIM);
        }

        // If the given entity is not an AbstractClientPlayer, return false
        return false;
    }

    /**
     * Ticks the client-side code.
     */
    public static void tickClient() {
        // Get the current player and the sound manager
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer ep = minecraft.player;
        SoundManager sound = minecraft.getSoundManager();

        // If the player and sound manager are not null, update the ambience sound
        if (ep != null && sound != null) {
            // Get the RegenerationData for the player, if it exists
            Optional<RegenerationData> optionalData = RegenerationData.get(ep);
            if (optionalData.isPresent()) {
                RegenerationData data = optionalData.get();

                // If the player is in the POST regen state and above the Zero Grid,
                // play the hum ambience sound
                if (data.regenState() == RegenStates.POST && PlayerUtil.isPlayerAboveZeroGrid(ep)) {
                    // Create the hum ambience sound if it doesn't exist
                    if (humAmbienceSound == null) {
                        humAmbienceSound = SimpleSoundInstance.forLocalAmbience(RSounds.GRACE_HUM.get(), 1, 1);
                    }

                    // Play the hum ambience sound if it is not already playing
                    if (!sound.isActive(humAmbienceSound)) {
                        sound.play(humAmbienceSound);
                    }
                } else {
                    // Stop the hum ambience sound if it is currently playing
                    if (sound.isActive(humAmbienceSound)) {
                        sound.stop(humAmbienceSound);
                    }
                }
            }
        }

        // Update the keybinds and destroy any unused textures
        RKeybinds.tickKeybinds();
        destroyTextures();
    }

    /**
     * Destroys any unused textures.
     */
    public static void destroyTextures() {
        // Get the current Minecraft instance and level
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        // If the level is null, clear the texture caches and release the textures
        if (level == null) {
            TextureManager textureManager = minecraft.getTextureManager();

            // Release the textures in the player skins cache and clear the cache
            if (!VisualManipulator.PLAYER_SKINS.isEmpty()) {
                VisualManipulator.PLAYER_SKINS.forEach((uuid, texture) -> textureManager.release(texture));
                VisualManipulator.PLAYER_SKINS.clear();
            }

            if (!TimelordRenderer.TIMELORDS.isEmpty()) {
                // Release the textures in the timelords cache and clear the cache
                TimelordRenderer.TIMELORDS.forEach((uuid, texture) -> textureManager.release(texture));
                TimelordRenderer.TIMELORDS.clear();
            }

            if (!JarTileRender.TEXTURES.isEmpty()) {
                // Release the textures in the jar tile render cache and clear the cache
                JarTileRender.TEXTURES.forEach((uuid, texture) -> textureManager.release(texture));
                JarTileRender.TEXTURES.clear();
            }

        }
    }

    public static ResourceLocation redirectSkin(UUID uuid) {
        if (Minecraft.getInstance().screen instanceof IncarnationScreen screen) {
            return IncarnationScreen.currentTexture;
        }

        if (VisualManipulator.PLAYER_SKINS.containsKey(uuid)) {
            return VisualManipulator.PLAYER_SKINS.get(uuid);
        }
        return null;
    }


    public static void clothingModels() {

        if (!ARMOR_MODELS.isEmpty()) return;

/*        ModelPart bakedGuard = Minecraft.getInstance().getEntityModels().bakeLayer(RModels.GUARD_ARMOR);
        GuardArmorModel guardHead = new GuardArmorModel(bakedGuard, EquipmentSlot.HEAD);
        GuardArmorModel guardChest = new GuardArmorModel(bakedGuard, EquipmentSlot.CHEST);
        GuardArmorModel guardLegs = new GuardArmorModel(bakedGuard, EquipmentSlot.LEGS);
        GuardArmorModel guardFeet = new GuardArmorModel(bakedGuard, EquipmentSlot.FEET);*/

        ModelPart bakedRobes = Minecraft.getInstance().getEntityModels().bakeLayer(RModels.COUNCIL_ROBES);
        ArmorModel robesHead = new ArmorModel(bakedRobes, EquipmentSlot.HEAD);
        ArmorModel robesChest = new ArmorModel(bakedRobes, EquipmentSlot.CHEST);
        ArmorModel robesLegs = new ArmorModel(bakedRobes, EquipmentSlot.LEGS);
        ArmorModel robesFeet = new ArmorModel(bakedRobes, EquipmentSlot.FEET);

        ModelPart bakedRobesSteve = Minecraft.getInstance().getEntityModels().bakeLayer(RModels.COUNCIL_ROBES_STEVE);
        ArmorModel robesChestSteve = new ArmorModel(bakedRobesSteve, EquipmentSlot.CHEST);

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
        ARMOR_MODELS_STEVE.put(RItems.GUARD_CHEST.get(), robesChestSteve);
        ARMOR_MODELS.put(RItems.GUARD_HELMET.get(), robesHead);
        ARMOR_MODELS.put(RItems.GUARD_CHEST.get(), robesChest);
        ARMOR_MODELS.put(RItems.GUARD_LEGS.get(), robesLegs);
        ARMOR_MODELS.put(RItems.GUARD_FEET.get(), robesFeet);

    }

    /**
     * Gets the armor model for the specified item stack and living entity.
     *
     * @param itemStack    the item stack
     * @param livingEntity the living entity
     * @return the armor model
     */
    public static HumanoidModel<?> getArmorModel(ItemStack itemStack, LivingEntity livingEntity) {
        // If the living entity is a player, check if it has a slim model
        if (livingEntity instanceof AbstractClientPlayer player) {

            PlayerInfo playerInfo = player.playerInfo;
            if (playerInfo == null) getHumanoidModel(itemStack, true);

            boolean isSlim = false;
            if (playerInfo != null && playerInfo.getSkin() != null) {
                isSlim = (Objects.equals(playerInfo.getSkin().model(), PlayerSkin.Model.SLIM));
            }

            // If the player has a slim model, return the slim armor model for the item stack
            if (isSlim) {
                return getHumanoidModel(itemStack, false);
            }
        }

        // Return the default armor model for the item stack
        return getHumanoidModel(itemStack, true);
    }


    /**
     * Gets the humanoid model for the specified item stack and model type.
     *
     * @param itemStack the item stack
     * @param trySteve  whether to try to get the Steve model, or the default model
     * @return the humanoid model, or null if no model was found
     */
    private static HumanoidModel<?> getHumanoidModel(ItemStack itemStack, boolean trySteve) {
        // If trySteve is true and the item stack is in the ARMOR_MODELS_STEVE map, return the Steve model for the item stack
        if (trySteve && ARMOR_MODELS_STEVE.containsKey(itemStack.getItem())) {
            return ARMOR_MODELS_STEVE.get(itemStack.getItem());
        }

        // If the item stack is in the ARMOR_MODELS map, return the default model for the item stack
        if (ARMOR_MODELS.containsKey(itemStack.getItem())) {
            return ARMOR_MODELS.get(itemStack.getItem());
        }

        // If no model was found, return null
        return null;
    }

    public static void doClientStuff() {
        itemPredicates();
        transitionTypes();
        renderers();
    }

    @ExpectPlatform
    public static void renderers() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void itemPredicates() {
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
        TransitionTypeRenderers.add(TransitionTypes.DRINK, DrinkTransitionRenderer.INSTANCE);
    }

    /**
     * Plays the specified positioned sound record.
     *
     * @param sound  the sound to play
     * @param pitch  the pitch of the sound
     * @param volume the volume of the sound
     */
    public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, pitch, volume));
    }

    /**
     * Plays the specified sound.
     *
     * @param entity        the entity associated with the sound
     * @param soundName     the name of the sound
     * @param category      the sound category
     * @param repeat        whether the sound should repeat
     * @param stopCondition a supplier that determines when the sound should stop
     * @param volume        the volume of the sound
     * @param randomSource  the random source for the sound
     */
    public static void playSound(Object entity, ResourceLocation soundName, SoundSource category, boolean repeat, Supplier<Boolean> stopCondition, float volume, RandomSource randomSource) {
        Minecraft.getInstance().getSoundManager().play(new MovingSound(entity, SoundEvent.createVariableRangeEvent(soundName), category, repeat, stopCondition, volume, randomSource));
    }

    /**
     * Creates a toast with the specified title and subtitle.
     *
     * @param title    the title of the toast
     * @param subtitle the subtitle of the toast
     */
    public static void createToast(MutableComponent title, MutableComponent subtitle) {
        Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT, title, subtitle));
    }

    /**
     * Sets the player's perspective based on the specified point of view.
     *
     * @param pointOfView the point of view to set the player's perspective to
     */
    public static void setPlayerPerspective(String pointOfView) {
        if (RegenConfig.CLIENT.changePerspective.get()) {
            Minecraft.getInstance().options.setCameraType(CameraType.valueOf(pointOfView));
        }
    }

    /**
     * Gets the player info for the specified player.
     *
     * @param player the player
     * @return the player info for the player
     */
    public static PlayerInfo getPlayerInfo(AbstractClientPlayer player) {
        return player.playerInfo;
    }
}
