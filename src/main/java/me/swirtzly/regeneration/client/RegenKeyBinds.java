package me.swirtzly.regeneration.client;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.network.MessageTriggerRegeneration;
import me.swirtzly.regeneration.network.NetworkHandler;
import me.swirtzly.regeneration.util.EnumCompatModids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Sub
 * on 17/09/2018.
 */
@EventBusSubscriber(Dist.CLIENT)
public class RegenKeyBinds {
	public static KeyBinding REGEN_NOW;
	public static KeyBinding REGEN_FORCEFULLY;
	
	public static void init() {
		
		if (!EnumCompatModids.LCCORE.isLoaded()) {
			REGEN_NOW = new KeyBinding("regeneration.keybinds.regenerate", GLFW.GLFW_KEY_R, RegenerationMod.NAME);
			ClientRegistry.registerKeyBinding(REGEN_NOW);
		}
		
		REGEN_FORCEFULLY = new KeyBinding("regeneration.keybinds.regenerate_forced", GLFW.GLFW_KEY_Y, RegenerationMod.NAME);
		ClientRegistry.registerKeyBinding(REGEN_FORCEFULLY);
	}
	
	
	@SubscribeEvent
	public static void keyInput(InputUpdateEvent e) {
		PlayerEntity player = Minecraft.getInstance().player;
		if (player == null || EnumCompatModids.LCCORE.isLoaded())
			return;

		CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> {git config
			if (REGEN_NOW.isPressed() && data.getState().isGraceful()) {
				NetworkHandler.INSTANCE.sendToServer(new MessageTriggerRegeneration());
			}
		});
	}
	
	/**
	 * Handles LCCore compatibility
	 */
	public static String getRegenerateNowDisplayName() {
		return REGEN_NOW.getTranslationKey();
	}
	
}
