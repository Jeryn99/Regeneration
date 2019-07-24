package me.swirtzly.regeneration.network;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class MessageSynchroniseRegeneration {
	
	private UUID player;
	private CompoundNBT data;
	
	public MessageSynchroniseRegeneration(UUID player, CompoundNBT data) {
		this.player = player;
		this.data = data;
	}
	
	public static void encode(MessageSynchroniseRegeneration message, PacketBuffer buffer) {
		buffer.writeUniqueId(message.player);
		buffer.writeCompoundTag(message.data);
	}
	
	public static MessageSynchroniseRegeneration decode(PacketBuffer packetBuffer) {
		return new MessageSynchroniseRegeneration(packetBuffer.readUniqueId(), packetBuffer.readCompoundTag());
	}
	
	public static class Handler {
		public static void handle(MessageSynchroniseRegeneration message, Supplier<NetworkEvent.Context> ctx) {
			PlayerEntity player = Minecraft.getInstance().world.getPlayerEntityByUUID(message.player);
			if (player != null)
				Minecraft.getInstance().addScheduledTask(() -> CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> data.deserializeNBT(message.data)));
			ctx.get().setPacketHandled(true);
		}
	}
	
}
