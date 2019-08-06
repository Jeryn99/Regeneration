package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.handlers.acting.ActingForwarder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateStateMessage {
	
	private PlayerEntity player;
	private String event;
	
	public UpdateStateMessage(PlayerEntity player, String event) {
		this.player = player;
		this.event = event;
	}
	
	public static void encode(UpdateStateMessage event, PacketBuffer packetBuffer) {
		packetBuffer.writeUniqueId(event.player.getUniqueID());
		packetBuffer.writeString(event.event);
	}
	
	public static UpdateStateMessage decode(PacketBuffer buffer) {
		if (Minecraft.getInstance().player == null)
			return null;
		return new UpdateStateMessage(Minecraft.getInstance().player.world.getPlayerByUuid(buffer.readUniqueId()), buffer.readString(600));
	}
	
	
	public static class Handler {
		public static void handle(UpdateStateMessage message, Supplier<NetworkEvent.Context> ctx) {
            Minecraft.getInstance().deferTask(() ->
                    RegenCap.get(message.player).ifPresent((data) -> ActingForwarder.onClient(ActingForwarder.RegenEvent.valueOf(message.event), data)));
			ctx.get().setPacketHandled(true);
		}
	}
	
}
