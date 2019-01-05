package me.fril.regeneration.client;

import me.fril.regeneration.Regeneration;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class RegenKeyBinds {
	
	public static KeyBinding REGEN_NOW;
	
	public static void init() {
        REGEN_NOW = new KeyBinding(new TextComponentTranslation("regeneration.keybinds.regenerate").getFormattedText(), Keyboard.KEY_R, Regeneration.NAME);
		ClientRegistry.registerKeyBinding(REGEN_NOW);
	}
	
}
