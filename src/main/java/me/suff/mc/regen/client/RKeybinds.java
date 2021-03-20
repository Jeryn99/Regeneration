package me.suff.mc.regen.client;

import me.suff.mc.regen.client.screen.PreferencesScreen;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.ForceRegenMessage;
import me.suff.mc.regen.network.messages.ToggleTraitMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class RKeybinds {
    public static KeyBinding FORCE_REGEN = new KeyBinding("Force Regeneration", GLFW.GLFW_KEY_Y, "Regeneration");
    public static KeyBinding REGEN_GUI = new KeyBinding("Open Preferences", GLFW.GLFW_KEY_F10, "Regeneration");
    public static KeyBinding TOGGLE_TRAIT = new KeyBinding("Toggle Trait", GLFW.GLFW_KEY_RIGHT_ALT, "Regeneration");

    public static void init() {
        ClientRegistry.registerKeyBinding(FORCE_REGEN);
        ClientRegistry.registerKeyBinding(REGEN_GUI);
        ClientRegistry.registerKeyBinding(TOGGLE_TRAIT);
    }


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
