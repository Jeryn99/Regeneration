package me.sub.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class PlayerUtil {

    public static void setWalkSpeed(EntityPlayerMP p, float speed) {
        ReflectionHelper.setPrivateValue(PlayerCapabilities.class, p.capabilities, speed, 6);
    }

}
