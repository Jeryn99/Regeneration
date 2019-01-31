package me.suff.regeneration.util;

import me.suff.regeneration.RegenerationMod;
import net.minecraft.entity.player.EntityPlayer;

public class DebuggerUtil {
	
	public static void out(EntityPlayer player, String message) {
		if (RegenerationMod.DEBUGGER != null) {
			RegenerationMod.DEBUGGER.getChannelFor(player).out(message);
		}
	}
	
	public static void warn(EntityPlayer player, String message) {
		if (RegenerationMod.DEBUGGER != null) {
			RegenerationMod.DEBUGGER.getChannelFor(player).warn(message);
		}
	}
}
