package me.fril.regeneration.util;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Map;

public class ClientUtil {
	
	public static void createToast(TextComponentTranslation title, TextComponentTranslation subtitle, RegenState regenState) {
		//Minecraft.getMinecraft().getToastGui().add(new ToastRegeneration(title, subtitle, regenState));
		//Minecraft.getMinecraft().getToastGui().add(new SystemToast(Type.TUTORIAL_HINT, title, subtitle));
	}
	
	public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(sound, pitch, volume));
	}

	public static NetworkPlayerInfo getNetworkPlayerInfo(AbstractClientPlayer player) {
		return ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
	}

	public static Map<MinecraftProfileTexture.Type, ResourceLocation> getSkinMap(AbstractClientPlayer player) {
		NetworkPlayerInfo playerInfo = getNetworkPlayerInfo(player);
		if (playerInfo == null) return null; // XXX NPE?
		return ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
	}

	public static void setPlayerTexture(AbstractClientPlayer player, ResourceLocation texture) {
		getSkinMap(player).put(MinecraftProfileTexture.Type.SKIN, texture);
		if (texture == null)
			ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, getNetworkPlayerInfo(player), false, 4);
	}
	
}
