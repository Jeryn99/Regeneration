package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateModel {
	
	private String preferred;
	
	
	public MessageUpdateModel() {
	}
	
	public MessageUpdateModel(String preferred) {
		this.preferred = preferred;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		preferred = ByteBufUtils.readUTF8String(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, preferred);
	}
	
	public static class Handler {
		public static void handle(MessageUpdateModel message, Supplier<NetworkEvent.Context > ctx){
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> {
				IRegeneration data = CapabilityRegeneration.getForPlayer(ctx.get().getSender());
				data.setPreferredModel(message.preferred);
				data.synchronise();
			});
		}
	}
}
