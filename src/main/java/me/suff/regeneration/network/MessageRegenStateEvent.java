package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.handlers.ActingForwarder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
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
		packetBuffer.writeString(event.event);
		packetBuffer.writeString(event.player.getGameProfile().getId().toString());
	}
	
	public static MessageRegenStateEvent decode(PacketBuffer buffer) {
		return new MessageRegenStateEvent(buffer.readString())
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (Minecraft.getInstance().player == null)
			return;
		player = Minecraft.getInstance().player.world.getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
		event = ByteBufUtils.readUTF8String(buf);
	}
	
	public static class Handler {
		public static void handle(MessageRegenStateEvent message, Supplier<NetworkEvent.Context> ctx) {
			Minecraft.getInstance().addScheduledTask(() -> ActingForwarder.onClient(message.event, CapabilityRegeneration.getForPlayer(message.player)));
		}
	}
	
}
