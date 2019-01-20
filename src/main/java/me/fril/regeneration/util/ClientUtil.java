package me.fril.regeneration.util;

import me.fril.regeneration.client.sound.MovingSoundPlayer;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.network.MessageUpdateSkin;
import me.fril.regeneration.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class ClientUtil {
	
	public static void createToast(TextComponentTranslation title, TextComponentTranslation subtitle, RegenState regenState) {
		//FIXME where ma toast at
		// Minecraft.getMinecraft().getToastGui().add(new ToastRegeneration(title, subtitle, regenState));
		// Minecraft.getMinecraft().getToastGui().add(new SystemToast(Type.TUTORIAL_HINT, title, subtitle));
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
	
	
	public static void sendSkinResetPacket() {
		NetworkHandler.INSTANCE.sendToServer(new MessageUpdateSkin(new byte[0], isSlimSkin(Minecraft.getMinecraft().player.getUniqueID())));
	}
	
	@SideOnly(Side.CLIENT)
	public static void playSound(String soundName, IRegeneration cap) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundPlayer(cap.getPlayer(), new SoundEvent(new ResourceLocation(soundName)), SoundCategory.PLAYERS, true, () -> !cap.getState().equals(RegenState.REGENERATING)));
	}
	
	
}
