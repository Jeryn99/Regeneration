package me.fril.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/** Notifies the server of a skin change */
public class MessageSkinChange implements IMessage {
	
	private boolean isAlex;
	private byte[] encodedSkin;
	
	public MessageSkinChange() {
	}
	
	public MessageSkinChange(byte[] pixelData, boolean isAlex) {
		this.encodedSkin = pixelData;
		this.isAlex = isAlex;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int length = buf.readInt();
		this.encodedSkin = new byte[length];
		for (int i = 0; i < length; i++) {
			this.encodedSkin[i] = buf.readByte();
		}
		
		this.isAlex = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.encodedSkin.length);
		for (int i = 0; i < this.encodedSkin.length; i++) {
			buf.writeByte(this.encodedSkin[i]);
		}
		buf.writeBoolean(isAlex);
	}
	
	
	
	public static class Handler implements IMessageHandler<MessageSkinChange, IMessage> {
		
		@Override
		public IMessage onMessage(MessageSkinChange message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				CapabilityRegeneration.getForPlayer(player).getSkinHandler().updateServerside(message.encodedSkin, message.isAlex);
			});
			return null;
		}
		
	}
}