package me.swirtzly.regen.util;

import me.swirtzly.regen.util.sound.MovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class ClientUtil {

    public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(sound, pitch, volume));
    }

    public static void playSound(Object entity, ResourceLocation soundName, SoundCategory category, boolean repeat, Supplier<Boolean> stopCondition, float volume) {
        Minecraft.getInstance().getSoundHandler().play(new MovingSound(entity, new SoundEvent(soundName), category, repeat, stopCondition, volume));
    }

    public static PointOfView getPlayerPerspective() {
        return Minecraft.getInstance().gameSettings.func_243230_g();
    }

    public static void setPlayerPerspective(PointOfView pointOfView) {
        //TODO Once the mod is in a functioning state, I want to add Vivecraft compat, Vivecraft doesnt have a concept of third person really
        Minecraft.getInstance().gameSettings.func_243229_a(pointOfView);
    }
}
