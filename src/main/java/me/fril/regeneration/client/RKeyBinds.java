package me.fril.regeneration.client;

import org.lwjgl.input.Keyboard;

import me.fril.regeneration.Regeneration;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class RKeyBinds {
	
	public static KeyBinding JUSTDOIT, GRACE;
	
	public static void init() {
		GRACE = new KeyBinding(I18n.translateToLocalFormatted("regeneration.keybinds.grace"), Keyboard.KEY_V, Regeneration.NAME);
		JUSTDOIT = new KeyBinding(I18n.translateToLocalFormatted("regeneration.keybinds.just_regen"), Keyboard.KEY_R, Regeneration.NAME);
		ClientRegistry.registerKeyBinding(GRACE);
		ClientRegistry.registerKeyBinding(JUSTDOIT);
	}
	
}
