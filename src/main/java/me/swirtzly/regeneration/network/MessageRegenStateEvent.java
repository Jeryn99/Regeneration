package me.swirtzly.regeneration.network;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.handlers.ActingForwarder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRegenStateEvent {
	
	private PlayerEntity player;
	private String event;
	
	public MessageRegenStateEvent(PlayerEntity player, String event) {
		this.player = player;
		this.event = event;
	}
	
	public static void encode(MessageRegenStateEvent event, PacketBuffer packetBuffer) {
		packetBuffer.writeUniqueId(event.player.getUniqueID());
		packetBuffer.writeString(event.event);
	}
	
	public static MessageRegenStateEvent decode(PacketBuffer buffer) {
		if (Minecraft.getInstance().player == null)
			return null;
		return new MessageRegenStateEvent(Minecraft.getInstance().player.world.getPlayerByUuid(buffer.readUniqueId()), buffer.readString(600));
	}
	
	
	public static class Handler {
		public static void handle(MessageRegenStateEvent message, Supplier<NetworkEvent.Context> ctx) {
			Minecraft.getInstance().runAsync(() ->
					CapabilityRegeneration.getForPlayer(message.player).ifPresent((data) -> ActingForwarder.onClient(ActingForwarder.RegenEvent.valueOf(message.event), data)));
			ctx.get().setPacketHandled(true);
		}
	}
	
}
