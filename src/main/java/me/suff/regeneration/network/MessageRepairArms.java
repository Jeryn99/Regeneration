package me.suff.regeneration.network;

import me.suff.regeneration.client.skinhandling.SkinInfo;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRepairArms {
	
	private SkinInfo.SkinType type;
	
	public MessageRepairArms(SkinInfo.SkinType type) {
		this.type = type;
	}
	
	public static void encode(MessageRepairArms event, PacketBuffer packetBuffer) {
		packetBuffer.writeString(event.type.name());
	}
	
	public static MessageRepairArms decode(PacketBuffer packetBuffer) {
		return new MessageRepairArms(SkinInfo.SkinType.valueOf(packetBuffer.readString(600)));
	}
	
	public static class Handler {
		public static void handle(MessageRepairArms message, Supplier<NetworkEvent.Context> ctx) {
			EntityPlayer player = ctx.get().getSender();
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> data.setVanillaType(message.type)));
			ctx.get().setPacketHandled(true);
		}
	}
	
}
