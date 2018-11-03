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
	
	public static KeyBinding REGEN_NOW, ENTER_GRACE;
	
	public static void init() {
		ENTER_GRACE = new KeyBinding(I18n.translateToLocalFormatted("regeneration.keybinds.grace"), Keyboard.KEY_V, RegenerationMod.NAME);
		REGEN_NOW = new KeyBinding(I18n.translateToLocalFormatted("regeneration.keybinds.just_regen"), Keyboard.KEY_R, RegenerationMod.NAME);
		ClientRegistry.registerKeyBinding(ENTER_GRACE);
		ClientRegistry.registerKeyBinding(REGEN_NOW);
	}
	
}
