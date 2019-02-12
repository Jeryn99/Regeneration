package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageUpdateModel {
	
	private String preferred;
	
	<MessageUpdateModel, IMessage> {
		
		@Override
		public static void handle (MessageUpdateModel message, Supplier < NetworkEvent.Context > ctx){
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				IRegeneration data = CapabilityRegeneration.getForPlayer(ctx.getServerHandler().player);
				data.setPreferredModel(message.preferred);
				data.synchronise();
			});
			
			return null;
		}
	}
	
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
	
	public static class Handler Handler
}
