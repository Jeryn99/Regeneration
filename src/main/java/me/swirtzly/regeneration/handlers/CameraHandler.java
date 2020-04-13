package me.swirtzly.regeneration.handlers;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class CameraHandler implements MouseWheelListener {

    private static float prevYaw = 0;
    private static float prevPitch = 0;
    private static float cameraRoll = 0;

    private static float playerOriginaLook = 0;
    private static float playerOriginalPitch = 0;
    private static float playerOriginalYawHead = 0;
    private static int playerOriginalSlotId = 0;
    private static double originalFOV = 0;

    private static boolean initFreeCam = true;
    private static boolean pendingExit = false;
    private static int prevScrollWheelKnot = 0;

    private static void manipulateFov(GameSettings gameSettings, double sensitivity) {
        prevScrollWheelKnot = 0;
        // TODO FIX ASAP
        prevScrollWheelKnot += Minecraft.getInstance().mouseHelper.getYVelocity();

        while (prevScrollWheelKnot >= 120) {
            prevScrollWheelKnot -= 120;
            gameSettings.fov -= sensitivity * 4;
        }
        while (prevScrollWheelKnot <= -120) {
            prevScrollWheelKnot += 120;
            gameSettings.fov += sensitivity * 4;
        }

        if (gameSettings.fov < 20) {
            gameSettings.fov = 20;
        }

        if (gameSettings.fov > 140) {
            gameSettings.fov = 140;
        }

        if (gameSettings.fov > 120 && gameSettings.thirdPersonView == 0) {
            gameSettings.thirdPersonView = 1;
            gameSettings.fov = 40;
        }

        if (gameSettings.fov < 40 && gameSettings.thirdPersonView == 1) {
            gameSettings.thirdPersonView = 0;
            gameSettings.fov = 120;
        }
    }

    public static void resetEverything(GameSettings gameSettings) {
        initFreeCam = true;
        prevYaw = 0;
        prevPitch = 0;
        gameSettings.fov = originalFOV;
        pendingExit = false;
    }

    @SubscribeEvent
    public void onView(EntityViewRenderEvent.CameraSetup event) {

      /*  Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        GameSettings gameSettings = mc.gameSettings;

        AtomicBoolean allowFreemode = new AtomicBoolean(false);

        RegenCap.get(player).ifPresent((data) -> {
            allowFreemode.set(data.getState() == PlayerUtil.RegenState.REGENERATING);

            if (data.getState() != PlayerUtil.RegenState.REGENERATING) {
                cameraRoll = 0;
            }
        });

        if (allowFreemode.get()) {
            if (gameSettings.keyBindLeft.isKeyDown()) {
                cameraRoll++;
            }
            if (gameSettings.keyBindRight.isKeyDown()) {
                cameraRoll--;
            }
        }

        event.setRoll(cameraRoll);

        if (allowFreemode.get()) {
            pendingExit = true;
            if (initFreeCam) {
                playerOriginaLook = player.rotationYaw;
                playerOriginalPitch = player.rotationPitch;
                playerOriginalYawHead = player.rotationYawHead;
                originalFOV = gameSettings.fov;
                playerOriginalSlotId = player.inventory.currentItem;
            }

            player.rotationYaw = playerOriginaLook;
            player.rotationPitch = playerOriginalPitch;
            player.rotationYawHead = playerOriginalYawHead;

            double sensitivity = Math.pow(gameSettings.mouseSensitivity * 0.6F + 0.2F, 3) * 8.0F;

            double deltaX = mc.mouseHelper.getMouseX() * sensitivity * 0.15D;
            double deltaY = -mc.mouseHelper.getMouseY() * sensitivity * 0.15D;

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
*/
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        prevScrollWheelKnot = e.getScrollAmount();
    }

}
