package me.swirtzly.regeneration.network;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageSynchronisationRequest {
	
	private UUID player;
	
	public MessageSynchronisationRequest(UUID player) {
		this.player = player;
	}
	
	public static void encode(MessageSynchronisationRequest message, PacketBuffer packetBuffer) {
		packetBuffer.writeUniqueId(message.player);
	}
	
	public static MessageSynchronisationRequest decode(PacketBuffer buffer) {
		return new MessageSynchronisationRequest(buffer.readUniqueId());
	}
	
	public static class Handler {
		public static void handle(MessageSynchronisationRequest message, Supplier<NetworkEvent.Context> ctx) {
			PlayerEntity player = ServerLifecycleHooks.getCurrentServer().getWorld(ctx.get().getSender().dimension).getPlayerByUuid(message.player);
			ctx.get().getSender().getServer().runAsync(() -> {
				if (player != null) {
					CapabilityRegeneration.getForPlayer(player).ifPresent(IRegeneration::synchronise);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
}
