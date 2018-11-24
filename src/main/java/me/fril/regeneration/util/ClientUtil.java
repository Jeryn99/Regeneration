package me.fril.regeneration.util;

import me.fril.regeneration.client.gui.ToastRegeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;

public class ClientUtil {
	
	public static void createToast(TextComponentTranslation title, TextComponentTranslation subtitle, RegenState regenState) {
		Minecraft.getMinecraft().getToastGui().add(new ToastRegeneration(title, subtitle, regenState));
	}
	
	public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(sound, pitch, volume));
	}
	
}
