package craig.software.mc.regen.client;

import craig.software.mc.regen.client.screen.PreferencesScreen;
import craig.software.mc.regen.network.NetworkDispatcher;
import craig.software.mc.regen.network.messages.ForceRegenMessage;
import craig.software.mc.regen.network.messages.ToggleTraitMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class RKeybinds {
    public static KeyMapping FORCE_REGEN = new KeyMapping("Force Regeneration", GLFW.GLFW_KEY_Y, "Regeneration");
    public static KeyMapping REGEN_GUI = new KeyMapping("Open Preferences", GLFW.GLFW_KEY_F10, "Regeneration");
    public static KeyMapping TOGGLE_TRAIT = new KeyMapping("Toggle Trait", GLFW.GLFW_KEY_RIGHT_ALT, "Regeneration");


    public static void tickKeybinds() {
        if (Minecraft.getInstance().level == null) return;
        if (FORCE_REGEN.consumeClick()) {
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ForceRegenMessage());
        }

        if (TOGGLE_TRAIT.consumeClick()) {
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ToggleTraitMessage());
        }

        if (REGEN_GUI.consumeClick() && Minecraft.getInstance().screen == null) {
            Minecraft.getInstance().submitAsync(() -> Minecraft.getInstance().setScreen(new PreferencesScreen()));
        }
    }
}
