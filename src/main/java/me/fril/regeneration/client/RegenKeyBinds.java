package me.fril.regeneration.client;

import org.lwjgl.input.Keyboard;

import me.fril.regeneration.RegenerationMod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class RegenKeyBinds {
	
	public static KeyBinding REGEN_NOW;
	
	public static void init() {
		REGEN_NOW = new KeyBinding(I18n.translateToLocalFormatted("regeneration.keybinds.regenerate"), Keyboard.KEY_R, RegenerationMod.NAME);
		ClientRegistry.registerKeyBinding(REGEN_NOW);
	}
	
}
