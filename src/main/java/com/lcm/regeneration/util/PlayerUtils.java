package com.lcm.regeneration.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PlayerUtils {

    public static void setWalkSpeed(EntityPlayerMP p, float speed) {
        ReflectionHelper.setPrivateValue(PlayerCapabilities.class, p.capabilities, speed, 6);
    }
}
