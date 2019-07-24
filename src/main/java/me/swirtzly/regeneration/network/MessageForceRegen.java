package me.swirtzly.regeneration.network;

import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageForceRegen {
	
	public static void encode(MessageForceRegen event, PacketBuffer packetBuffer) {
	
	}
	
	public static MessageForceRegen decode(PacketBuffer buffer) {
		return new MessageForceRegen();
	}
	
	
	public static class Handler {
		public static void handle(MessageForceRegen message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().getSender().getServer().runAsync(() -> {
				ServerPlayerEntity player = ctx.get().getSender();
				if (player != null) {
					player.attackEntityFrom(RegenObjects.REGEN_DMG_KILLED, 99F);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
}
