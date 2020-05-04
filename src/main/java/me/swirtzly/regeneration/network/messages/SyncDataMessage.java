package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncDataMessage {

	private Entity player;

	public SyncDataMessage(Entity player) {
		this.player = player;
	}
	
	public static void encode(SyncDataMessage message, PacketBuffer packetBuffer) {
		packetBuffer.writeInt(message.player.getEntityId());
	}
	
	public static SyncDataMessage decode(PacketBuffer buffer) {
		return new SyncDataMessage(Minecraft.getInstance().player.world.getEntityByID(buffer.readInt()));
	}
	
	public static class Handler {
		public static void handle(SyncDataMessage message, Supplier<NetworkEvent.Context> ctx) {
			Entity player = message.player;
            ctx.get().getSender().getServer().deferTask(() -> {
				if (player != null) {
                    RegenCap.get(player).ifPresent(IRegen::synchronise);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
}
