package me.swirtzly.regeneration.client;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Mouse;

@EventBusSubscriber(value = Side.CLIENT)
public class CameraHandler {

    private static float prevYaw = 0;
    private static float prevPitch = 0;
    private static float cameraRoll = 0;

    private static float playerOriginaLook = 0;
    private static float playerOriginalPitch = 0;
    private static float playerOriginalYawHead = 0;
    private static int playerOriginalSlotId = 0;
    private static float originalFOV = 0;

    private static boolean initFreeCam = true;
    private static boolean pendingExit = false;

    @SubscribeEvent
    public static void onView(EntityViewRenderEvent.CameraSetup event) {

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        GameSettings gameSettings = mc.gameSettings;

        IRegeneration data = CapabilityRegeneration.getForPlayer(player);

        boolean allowFreemode = data.getState() == PlayerUtil.RegenState.REGENERATING || data.isSyncingToJar();

        if (allowFreemode) {
            if (gameSettings.keyBindLeft.isKeyDown()) {
                cameraRoll++;
            }
            if (gameSettings.keyBindRight.isKeyDown()) {
                cameraRoll--;
            }
        }

        if (data.getState() != PlayerUtil.RegenState.REGENERATING) {
            cameraRoll = 0;
        }

        event.setRoll(cameraRoll);


        if (allowFreemode) {
            pendingExit = true;
            if (initFreeCam) {
                playerOriginaLook = player.rotationYaw;
                playerOriginalPitch = player.rotationPitch;
                playerOriginalYawHead = player.rotationYawHead;
                originalFOV = gameSettings.fovSetting;
                playerOriginalSlotId = player.inventory.currentItem;
            }

            player.rotationYaw = playerOriginaLook;
            player.rotationPitch = playerOriginalPitch;
            player.rotationYawHead = playerOriginalYawHead;

            double sensitivity = Math.pow(gameSettings.mouseSensitivity * 0.6F + 0.2F, 3) * 8.0F;

            double deltaX = mc.mouseHelper.deltaX * sensitivity * 0.15D;
            double deltaY = -mc.mouseHelper.deltaY * sensitivity * 0.15D;

            event.setYaw((float) deltaX + prevYaw + playerOriginaLook - 180);
            event.setPitch((float) deltaY + prevPitch + player.rotationPitch);
            prevYaw = (float) deltaX + prevYaw;
            prevPitch = (float) deltaY + prevPitch;

            player.inventory.currentItem = playerOriginalSlotId;

            manipulateFov(gameSettings, sensitivity);

            initFreeCam = false;
        } else {
            if (pendingExit) {
                resetEverything(gameSettings);
            }
        }

    }

    private static void manipulateFov(GameSettings gameSettings, double sensitivity) {
        int prevScrollWheelKnot = 0;
        prevScrollWheelKnot += Mouse.getDWheel();

        while (prevScrollWheelKnot >= 120) {
            prevScrollWheelKnot -= 120;
            gameSettings.fovSetting -= sensitivity * 4;
        }
        while (prevScrollWheelKnot <= -120) {
            prevScrollWheelKnot += 120;
            gameSettings.fovSetting += sensitivity * 4;
        }

        if (gameSettings.fovSetting < 20) {
            gameSettings.fovSetting = 20;
        }

        if (gameSettings.fovSetting > 140) {
            gameSettings.fovSetting = 140;
        }

        if (gameSettings.fovSetting > 120 && gameSettings.thirdPersonView == 0) {
            gameSettings.thirdPersonView = 1;
            gameSettings.fovSetting = 40;
        }

        if (gameSettings.fovSetting < 40 && gameSettings.thirdPersonView == 1) {
            gameSettings.thirdPersonView = 0;
            gameSettings.fovSetting = 120;
        }
    }


    public static void resetEverything(GameSettings gameSettings) {
        initFreeCam = true;
        prevYaw = 0;
        prevPitch = 0;
        gameSettings.fovSetting = originalFOV;
        pendingExit = false;
    }

}