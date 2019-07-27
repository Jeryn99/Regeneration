package me.swirtzly.regeneration.network;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateSkinMapMessage {
	
	private String preferred;
	
	public UpdateSkinMapMessage(String preferred) {
		this.preferred = preferred;
	}
	
	public static void encode(UpdateSkinMapMessage model, PacketBuffer buf) {
		buf.writeString(model.preferred);
	}
	
	public static UpdateSkinMapMessage decode(PacketBuffer buffer) {
		return new UpdateSkinMapMessage(buffer.readString(10));
	}
	
	public static class Handler {
		public static void handle(UpdateSkinMapMessage message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().getSender().getServer().runAsync(() -> CapabilityRegeneration.getForPlayer(ctx.get().getSender()).ifPresent((cap) -> {
				cap.setPreferredModel(message.preferred);
				cap.synchronise();
			}));
			ctx.get().setPacketHandled(true);
		}
	}
}
