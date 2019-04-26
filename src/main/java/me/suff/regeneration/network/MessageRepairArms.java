package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.client.skinhandling.SkinInfo;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRepairArms implements IMessage {
	
	private String arm;
	
	public MessageRepairArms(SkinInfo.SkinType type){
		this.arm = type.name();
	}
	
	public MessageRepairArms(){}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		arm = ByteBufUtils.readUTF8String(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, arm);
	}
	
	public static class Handler implements IMessageHandler<MessageRepairArms, IMessage> {
		
		@Override
		public IMessage onMessage(MessageRepairArms message, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				IRegeneration data = CapabilityRegeneration.getForPlayer(ctx.getServerHandler().player);
				data.setVanillaSkinType(SkinInfo.SkinType.valueOf(message.arm));
				System.out.println(message.arm);
			});
			return null;
		}
	}
}
