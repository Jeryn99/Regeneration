package me.fril.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageRegenerationStyle implements IMessage {
	
	private NBTTagCompound style;
	
	public MessageRegenerationStyle() {}
	
	public MessageRegenerationStyle(NBTTagCompound nbtTagCompound) {
		style = nbtTagCompound;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		style = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, style);
	}
	
	public static class Handler implements IMessageHandler<MessageRegenerationStyle, IMessage> {
		@Override
		public IMessage onMessage(MessageRegenerationStyle message, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(()-> {
				IRegeneration cap = CapabilityRegeneration.getForPlayer(ctx.getServerHandler().player);
				cap.setStyle(message.style);
				cap.sync();
			});
			return null;
		}
	}
}
