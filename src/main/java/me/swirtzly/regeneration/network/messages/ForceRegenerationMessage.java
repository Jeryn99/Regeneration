package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ForceRegenerationMessage {
	
	public static void encode(ForceRegenerationMessage event, PacketBuffer packetBuffer) {
	
	}
	
	public static ForceRegenerationMessage decode(PacketBuffer buffer) {
		return new ForceRegenerationMessage();
	}
	
	
	public static class Handler {
		public static void handle(ForceRegenerationMessage message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().getSender().getServer().deferTask(() -> {
				ServerPlayerEntity player = ctx.get().getSender();
				RegenCap.get(player).ifPresent((data) -> {
                    if (data.getState() == PlayerUtil.RegenState.ALIVE && data.getRegenerationsLeft() > 0) {
						player.attackEntityFrom(RegenObjects.REGEN_DMG_FORCED, Integer.MAX_VALUE);
					}
				});
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
}
