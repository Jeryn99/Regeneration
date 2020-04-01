package me.swirtzly.regeneration.asm;

import me.swirtzly.regeneration.common.capability.RegenCap;
import net.minecraft.client.Minecraft;

import static me.swirtzly.regeneration.util.PlayerUtil.RegenState.GRACE_CRIT;

public class RegenClientHooks {

    private static boolean enabled() {
        return Minecraft.getInstance().player != null && RegenCap.get(Minecraft.getInstance().player).orElse(null).getState() == GRACE_CRIT;
    }
	
}
