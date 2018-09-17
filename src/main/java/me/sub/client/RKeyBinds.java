package me.sub.client;

import me.sub.Regeneration;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class RKeyBinds {

    public static KeyBinding GRACE;

    public static void init() {
        GRACE = new KeyBinding("regen.keybinds.grace", Keyboard.KEY_C, Regeneration.NAME);
        ClientRegistry.registerKeyBinding(GRACE);
    }

}
