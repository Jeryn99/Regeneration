package me.suff.regeneration.network;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.handlers.ActingForwarder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageRegenStateEvent {
	
	private EntityPlayer player;
	private String event;
	
	public MessageRegenStateEvent() {
	}
	
	public MessageRegenStateEvent(EntityPlayer player, String event) {
		this.player = player;
		this.event = event;
	}
	
	public static void encode(MessageRegenStateEvent event, PacketBuffer packetBuffer) {
		packetBuffer.writeString(event.player.getGameProfile().getId().toString());
		packetBuffer.writeString(event.event);
	}
	
	public static MessageRegenStateEvent decode(PacketBuffer buffer) {
		if (Minecraft.getInstance().player == null)
			return null;
		return new MessageRegenStateEvent(Minecraft.getInstance().player.world.getPlayerEntityByUUID(UUID.fromString(buffer.readString(600))), buffer.readString(600));
	}
	
	
	public static class Handler {
		public static void handle(MessageRegenStateEvent message, Supplier<NetworkEvent.Context> ctx) {
			Minecraft.getInstance().addScheduledTask(() -> ActingForwarder.onClient(message.event, CapabilityRegeneration.getForPlayer(message.player)));
			ctx.get().setPacketHandled(true);
		}
	}
	
}
