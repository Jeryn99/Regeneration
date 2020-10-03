package me.swirtzly.regen.util;

import me.swirtzly.regen.util.sound.MovingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;

import java.util.function.Supplier;

public class ClientUtil {

    public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(sound, pitch, volume));
    }

    public static void playSound(Object entity, ResourceLocation soundName, SoundCategory category, boolean repeat, Supplier<Boolean> stopCondition, float volume) {
        Minecraft.getInstance().getSoundHandler().play(new MovingSound(entity, new SoundEvent(soundName), category, repeat, stopCondition, volume));
    }

    public static PointOfView getPlayerPerspective() {
        return Minecraft.getInstance().gameSettings.getPointOfView();
    }

    public static void setPlayerPerspective(PointOfView pointOfView) {
        //TODO Once the mod is in a functioning state, I want to add Vivecraft compat, Vivecraft doesnt have a concept of third person really
        Minecraft.getInstance().gameSettings.setPointOfView(pointOfView);
    }

    public static double calculateColorBrightness(Vector3d c) {
        float r = (float) c.x, g = (float) c.y, b = (float) c.z;
        r = r <= 0.03928 ? r / 12.92F : (float) Math.pow((r + 0.055) / 1.055, 2.4);
        g = g <= 0.03928 ? g / 12.92F : (float) Math.pow((g + 0.055) / 1.055, 2.4);
        b = b <= 0.03928 ? b / 12.92F : (float) Math.pow((b + 0.055) / 1.055, 2.4);

        return (0.2126 * r) + (0.7152 * g) + (0.0722 * b);
    }
}
