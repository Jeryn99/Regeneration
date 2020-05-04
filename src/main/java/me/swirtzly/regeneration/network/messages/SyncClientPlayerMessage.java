package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.common.capability.RegenCap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by Sub on 16/09/2018.
 */
public class SyncClientPlayerMessage {

	private Entity player;
	private CompoundNBT data;

	public SyncClientPlayerMessage(Entity player, CompoundNBT data) {
		this.player = player;
		this.data = data;
	}
	
	public static void encode(SyncClientPlayerMessage message, PacketBuffer buffer) {
		buffer.writeInt(message.player.getEntityId());
		buffer.writeCompoundTag(message.data);
	}
	
	public static SyncClientPlayerMessage decode(PacketBuffer packetBuffer) {
		return new SyncClientPlayerMessage(Minecraft.getInstance().player.world.getEntityByID(packetBuffer.readInt()), packetBuffer.readCompoundTag());
	}
	
	public static class Handler {
		public static void handle(SyncClientPlayerMessage message, Supplier<NetworkEvent.Context> ctx) {
			Entity player = message.player;
            if (player != null)
                Minecraft.getInstance().deferTask(() -> RegenCap.get(player).ifPresent((data) -> data.deserializeNBT(message.data)));
			ctx.get().setPacketHandled(true);
		}
	}
	
}
