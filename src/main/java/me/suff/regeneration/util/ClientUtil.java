package me.suff.regeneration.util;

import io.netty.buffer.Unpooled;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.network.MessageUpdateSkin;
import me.suff.regeneration.network.NetworkHandler;
import me.suff.regeneration.util.client.MovingSoundEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class ClientUtil {
	
	public static String keyBind = "???"; //WAFFLE there was a weird thing with this somewhere that I still need to fix
	
	public static void createToast(TextComponentTranslation title, TextComponentTranslation subtitle) {
		Minecraft.getInstance().getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, title, subtitle));
	}
	
	public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
		Minecraft.getInstance().getSoundHandler().play(SimpleSound.getRecord(sound, pitch, volume));
	}
	
	
	public static void sendSkinResetPacket() {
		PacketBuffer output = new PacketBuffer(Unpooled.buffer());
		output.writeBytes(new byte[0]);
		NetworkHandler.sendToServer(new MessageUpdateSkin(output, SkinChangingHandler.wasAlex(Minecraft.getInstance().player)));
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void playSound(Entity entity, ResourceLocation soundName, SoundCategory category, boolean repeat, Supplier<Boolean> stopCondition, float volume) {
		if (entity.world.isRemote) {
			Minecraft.getInstance().getSoundHandler().play(new MovingSoundEntity(entity, new SoundEvent(soundName), category, repeat, stopCondition, volume));
		}
	}
	
	
}
