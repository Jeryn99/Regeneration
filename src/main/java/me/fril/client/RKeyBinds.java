package me.fril.client;

import me.fril.Regeneration;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;


/**
 * Created by Sub
 * on 17/09/2018.
 */
public class RKeyBinds {

    public static KeyBinding JUSTDOIT, GRACE;

    public static void init() {
        GRACE = new KeyBinding("regen.keybinds.grace", Keyboard.KEY_V, Regeneration.NAME);
        JUSTDOIT = new KeyBinding("regen.keybinds.just_regen", Keyboard.KEY_R, Regeneration.NAME);
        ClientRegistry.registerKeyBinding(GRACE);
        ClientRegistry.registerKeyBinding(JUSTDOIT);
    }

}
