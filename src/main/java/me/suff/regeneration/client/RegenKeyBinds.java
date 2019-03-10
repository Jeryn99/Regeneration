package me.suff.regeneration.client;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.util.EnumCompatModids;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Sub
 * on 17/09/2018.
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = RegenerationMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegenKeyBinds {
	public static KeyBinding REGEN_NOW;
	public static KeyBinding REGEN_FORCEFULLY;
	public static KeyBinding REGEN_CUSTOMISE;
	
	public static void init() {
		
		if (!EnumCompatModids.LCCORE.isLoaded()) {
			REGEN_NOW = new KeyBinding("regeneration.keybinds.regenerate", GLFW.GLFW_KEY_R, RegenerationMod.NAME);
			ClientRegistry.registerKeyBinding(REGEN_NOW);
		}
		
		REGEN_FORCEFULLY = new KeyBinding("regeneration.keybinds.regenerate_forced", GLFW.GLFW_KEY_L, RegenerationMod.NAME);
		ClientRegistry.registerKeyBinding(REGEN_FORCEFULLY);
		
		REGEN_CUSTOMISE = new KeyBinding("regeneration.keybinds.customize", GLFW.GLFW_KEY_F10, RegenerationMod.NAME);
		ClientRegistry.registerKeyBinding(REGEN_CUSTOMISE);
		
	}
	
	/**
	 * Handles LCCore compatibility
	 */
	public static String getRegenerateNowDisplayName() {
		if(REGEN_NOW == null){
			return "WHY";
		}
		return REGEN_NOW.getTranslationKey();
	}
	
}
