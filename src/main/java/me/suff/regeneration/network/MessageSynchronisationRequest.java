package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageSynchronisationRequest {
	
	private int dim;
	private UUID player;
	
	public MessageSynchronisationRequest() {
	}
	
	public MessageSynchronisationRequest(UUID player, int dim) {
		this.player = player;
		this.dim = dim;
	}
	
	public static void encode(MessageSynchronisationRequest message, PacketBuffer packetBuffer){
		packetBuffer.writeUniqueId(message.player);
		packetBuffer.writeInt(message.dim);
	}
	
	public static MessageSynchronisationRequest decode(PacketBuffer buffer){
		return new MessageSynchronisationRequest(buffer.readUniqueId(), buffer.readInt());
	}
	
	public static class Handler {
		public static void handle(MessageSynchronisationRequest message, Supplier<NetworkEvent.Context> ctx) {
			EntityPlayer player = ServerLifecycleHooks.getCurrentServer().getWorld(ctx.get().getSender().dimension).getPlayerEntityByUUID(message.player);
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> CapabilityRegeneration.getForPlayer(player).synchronise());
		}
	}
	
}
