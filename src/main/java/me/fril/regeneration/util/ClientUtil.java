package me.fril.regeneration.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.toasts.SystemToast.Type;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.UUID;

public class ClientUtil {
	
	public static void createToast(TextComponentTranslation title, TextComponentTranslation subtitle, RegenState regenState) {
		//Minecraft.getMinecraft().getToastGui().add(new ToastRegeneration(title, subtitle, regenState));
		//Minecraft.getMinecraft().getToastGui().add(new SystemToast(Type.TUTORIAL_HINT, title, subtitle));
	}
	
	public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(sound, pitch, volume));
	}

	/**
	 * Checks if a players skin model is slim or the default. The Alex model is slime while the Steve model is default.
	 */
	public static boolean isSlimSkin(UUID playerUUID) {
		return (playerUUID.hashCode() & 1) == 1;
	}
	
}
