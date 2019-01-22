package me.fril.regeneration.util;

import me.fril.regeneration.client.gui.ToastRegeneration;
import me.fril.regeneration.client.sound.MovingSoundEntity;
import me.fril.regeneration.network.MessageUpdateSkin;
import me.fril.regeneration.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientUtil {
	
	public static String keyBind = "???";
	
	public static void createToast(TextComponentTranslation title, TextComponentTranslation subtitle, RegenState regenState) {
		//FIXME where ma toast at
		Minecraft.getMinecraft().getToastGui().add(new ToastRegeneration(title, subtitle, regenState));
		Minecraft.getMinecraft().getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, title, subtitle));
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
	public static void playSound(Entity entity, String soundName, Supplier<Boolean> stopCondition, boolean repeat) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundEntity(entity, new SoundEvent(new ResourceLocation(soundName)), SoundCategory.PLAYERS, repeat, stopCondition));
	}
	
	
}
