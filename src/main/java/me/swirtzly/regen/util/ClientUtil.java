package me.swirtzly.regen.util;

import me.swirtzly.regen.util.sound.MovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class ClientUtil {

    public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(sound, pitch, volume));
    }

    @OnlyIn(Dist.CLIENT)
    public static void playSound(Object entity, ResourceLocation soundName, SoundCategory category, boolean repeat, Supplier<Boolean> stopCondition, float volume) {
        Minecraft.getInstance().getSoundHandler().play(new MovingSound(entity, new SoundEvent(soundName), category, repeat, stopCondition, volume));
    }
}
