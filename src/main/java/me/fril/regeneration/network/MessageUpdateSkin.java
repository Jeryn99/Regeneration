package me.fril.regeneration.network;


import io.netty.buffer.ByteBuf;
import me.fril.regeneration.client.skinhandling.SkinInfo;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.RegenState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageUpdateSkin implements IMessage {
	
	private boolean isAlex;
	private byte[] encodedSkin;
	
	public MessageUpdateSkin() {
	}
	
	public MessageUpdateSkin(byte[] pixelData, boolean isAlex) {
		encodedSkin = pixelData;
		this.isAlex = isAlex;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int length = buf.readInt();
		this.encodedSkin = new byte[length];
		for (int i = 0; i < length; i++) {
			this.encodedSkin[i] = buf.readByte();
		}
		
		isAlex = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.encodedSkin.length);
		for (int i = 0; i < this.encodedSkin.length; i++) {
			buf.writeByte(this.encodedSkin[i]);
		}
		buf.writeBoolean(isAlex);
	}
	
	public static class Handler implements IMessageHandler<MessageUpdateSkin, IMessage> {
		@Override
		public IMessage onMessage(MessageUpdateSkin message, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				EntityPlayerMP player = ctx.getServerHandler().player;
				IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
				if (cap.getState().equals(RegenState.REGENERATING)) {
					cap.setEncodedSkin(message.encodedSkin);
					if (message.isAlex) {
						cap.setSkinType(SkinInfo.SkinType.ALEX.name());
					} else {
						cap.setSkinType(SkinInfo.SkinType.STEVE.name());
					}
					cap.synchronise();
					NetworkHandler.INSTANCE.sendToAll(new MessageRemovePlayer(player.getUniqueID().toString()));
				}
			});
			return null;
		}
	}
}

