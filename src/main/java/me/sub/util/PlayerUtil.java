package me.sub.util;

import me.sub.MovingSoundPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class PlayerUtil {

    public static void setWalkSpeed(EntityPlayerMP p, float speed) {
        ReflectionHelper.setPrivateValue(PlayerCapabilities.class, p.capabilities, speed, 6);
    }

    @SideOnly(Side.CLIENT)
    public static void playMovingSound(EntityPlayer player, SoundEvent soundIn, SoundCategory categoryIn) {
        System.out.println(soundIn.getSoundName());
        Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundPlayer(player, soundIn, categoryIn));
    }

}
