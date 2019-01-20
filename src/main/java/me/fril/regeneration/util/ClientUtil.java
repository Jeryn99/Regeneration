package me.fril.regeneration.util;

import me.fril.regeneration.network.MessageSkinChange;
import me.fril.regeneration.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;

public class ClientUtil {
	
	public static void createToast(TextComponentTranslation title, TextComponentTranslation subtitle, RegenState regenState) {
		//FIXME where ma toast at
		// Minecraft.getMinecraft().getToastGui().add(new ToastRegeneration(title, subtitle, regenState));
		// Minecraft.getMinecraft().getToastGui().add(new SystemToast(Type.TUTORIAL_HINT, title, subtitle));
	}
	
	public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(sound, pitch, volume));
	}
	
	public static void sendSkinResetPacket() {
		NetworkHandler.INSTANCE.sendToServer(new MessageSkinChange(new byte[0], Minecraft.getMinecraft().player.getSkinType().equals("slim")));
	}
	
	
}
