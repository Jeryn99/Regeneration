package me.suff.regeneration.network;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.entity.player.EntityPlayer;
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
			EntityPlayer player = ServerLifecycleHooks.getCurrentServer().getWorld(ctx.get().getSender().dimension).getPlayerEntityByUUID(message.player);
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> data.sync()));
			ctx.get().setPacketHandled(true);
		}
	}
	
}
