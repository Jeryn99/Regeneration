package me.sub.client;

import org.lwjgl.input.Keyboard;

import me.sub.Regeneration;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;


/**
 * Created by Sub
 * on 17/09/2018.
 */
public class RKeyBinds {

    public static KeyBinding JUSTDOIT;
    public static KeyBinding GRACE;

    public static void init() {
        GRACE = new KeyBinding("regen.keybinds.grace", Keyboard.KEY_V, Regeneration.NAME);
        JUSTDOIT = new KeyBinding("regen.keybinds.just_regen", Keyboard.KEY_R, Regeneration.NAME);
        ClientRegistry.registerKeyBinding(GRACE);
        ClientRegistry.registerKeyBinding(JUSTDOIT);
    }

}
