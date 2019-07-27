package me.swirtzly.regeneration.network;

import me.swirtzly.regeneration.client.skinhandling.SkinInfo;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AdjustArmsMessage {
	
	private SkinInfo.SkinType type;
	
	public AdjustArmsMessage(SkinInfo.SkinType type) {
		this.type = type;
	}
	
	public static void encode(AdjustArmsMessage event, PacketBuffer packetBuffer) {
		packetBuffer.writeString(event.type.name());
	}
	
	public static AdjustArmsMessage decode(PacketBuffer packetBuffer) {
		return new AdjustArmsMessage(SkinInfo.SkinType.valueOf(packetBuffer.readString(600)));
	}
	
	public static class Handler {
		public static void handle(AdjustArmsMessage message, Supplier<NetworkEvent.Context> ctx) {
			PlayerEntity player = ctx.get().getSender();
			ctx.get().getSender().getServer().runAsync(() -> CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> data.setVanillaSkinType(message.type)));
			ctx.get().setPacketHandled(true);
		}
	}
	
}
