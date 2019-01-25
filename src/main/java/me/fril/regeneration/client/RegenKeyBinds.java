package me.fril.regeneration.client;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.network.MessageTriggerRegeneration;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.EnumCompatModids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

/**
 * Created by Sub
 * on 17/09/2018.
 */
@EventBusSubscriber(Side.CLIENT)
public class RegenKeyBinds {
	private static KeyBinding REGEN_NOW;
	
	public static void init() {
		if (!EnumCompatModids.LCCORE.isLoaded()) {
			REGEN_NOW = new KeyBinding("regeneration.keybinds.regenerate", Keyboard.KEY_R, RegenerationMod.NAME);
			ClientRegistry.registerKeyBinding(REGEN_NOW);
		}
	}
	
	@SubscribeEvent
	public static void keyInput(InputUpdateEvent e) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player == null || EnumCompatModids.LCCORE.isLoaded())
			return;
		
		if (REGEN_NOW.isPressed() && CapabilityRegeneration.getForPlayer(player).getState().isGraceful()) {
			NetworkHandler.INSTANCE.sendToServer(new MessageTriggerRegeneration(player));
		}
	}
	
	/**
	 * Handles LCCore compatibility
	 */
	public static String getRegenerateNowDisplayName() {
		return REGEN_NOW.getDisplayName();
	}
	
}
