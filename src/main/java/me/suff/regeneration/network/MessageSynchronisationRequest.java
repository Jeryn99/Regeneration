package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class MessageSynchronisationRequest {
	
	private EntityPlayer player;
	
	public MessageSynchronisationRequest() {
	}
	
	public MessageSynchronisationRequest(EntityPlayer player) {
		this.player = player;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(player.dimension);
		ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int dim = buf.readInt();
		player = ServerLifecycleHooks.getCurrentServer().getWorld(dim).getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
	}
	
	public static class Handler {
		public static void handle(MessageSynchronisationRequest message, Supplier<NetworkEvent.Context> ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> CapabilityRegeneration.getForPlayer(message.player).synchronise());
			
		}
	}
	
}
