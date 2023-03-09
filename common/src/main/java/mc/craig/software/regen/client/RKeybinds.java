package mc.craig.software.regen.client;

import mc.craig.software.regen.client.screen.PreferencesScreen;
import mc.craig.software.regen.network.messages.ForceRegenMessage;
import mc.craig.software.regen.network.messages.ToggleTraitMessage;
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
            new ForceRegenMessage().send();
        }

        if (TOGGLE_TRAIT.consumeClick()) {
            new ToggleTraitMessage().send();
        }

        if (REGEN_GUI.consumeClick() && Minecraft.getInstance().screen == null) {
            Minecraft.getInstance().submit(() -> Minecraft.getInstance().setScreen(new PreferencesScreen()));
        }
    }
}
