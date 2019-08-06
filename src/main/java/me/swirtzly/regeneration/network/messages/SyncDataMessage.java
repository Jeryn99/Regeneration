package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncDataMessage {
	
	private UUID player;
	
	public SyncDataMessage(UUID player) {
		this.player = player;
	}
	
	public static void encode(SyncDataMessage message, PacketBuffer packetBuffer) {
		packetBuffer.writeUniqueId(message.player);
	}
	
	public static SyncDataMessage decode(PacketBuffer buffer) {
		return new SyncDataMessage(buffer.readUniqueId());
	}
	
	public static class Handler {
		public static void handle(SyncDataMessage message, Supplier<NetworkEvent.Context> ctx) {
			PlayerEntity player = ServerLifecycleHooks.getCurrentServer().getWorld(ctx.get().getSender().dimension).getPlayerByUuid(message.player);
            ctx.get().getSender().getServer().deferTask(() -> {
				if (player != null) {
                    RegenCap.get(player).ifPresent(IRegen::synchronise);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
}
