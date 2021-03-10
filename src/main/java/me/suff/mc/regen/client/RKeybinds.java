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
        if (Minecraft.getInstance().level == null) return;
        if (FORCE_REGEN.consumeClick()) {
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ForceRegenMessage());
        }

        if (REGEN_GUI.consumeClick() && Minecraft.getInstance().screen == null) {
            Minecraft.getInstance().submitAsync(() -> Minecraft.getInstance().setScreen(new PreferencesScreen()));
        }
    }
}
