package me.suff.regeneration.network;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.handlers.ActingForwarder;
import me.suff.regeneration.handlers.RegenObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
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
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> {
				EntityPlayerMP player = ctx.get().getSender();
				if (player != null) {
					player.attackEntityFrom(RegenObjects.REGEN_DMG_FORCED, 99F);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
}
