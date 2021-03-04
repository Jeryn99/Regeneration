package me.suff.mc.regen.client;

import me.suff.mc.regen.client.gui.PreferencesScreen;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.ForceRegenMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class RKeybinds {
    public static KeyBinding FORCE_REGEN = new KeyBinding("Force Regeneration", 89, "Regeneration");
    public static KeyBinding REGEN_GUI = new KeyBinding("Open Preferences", 299, "Regeneration");

    public static void init() {
        ClientRegistry.registerKeyBinding(FORCE_REGEN);
        ClientRegistry.registerKeyBinding(REGEN_GUI);
    }


    public static void tickKeybinds() {
        if (Minecraft.getInstance().world == null) return;
        if (FORCE_REGEN.isPressed()) {
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ForceRegenMessage());
        }

        if (REGEN_GUI.isPressed() && Minecraft.getInstance().currentScreen == null) {
            Minecraft.getInstance().deferTask(() -> Minecraft.getInstance().displayGuiScreen(new PreferencesScreen()));
        }
    }
}
