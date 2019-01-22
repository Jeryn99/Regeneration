package me.fril.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.fril.regeneration.client.skinhandling.SkinChangingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageRemovePlayer implements IMessage {
	
	private UUID playerUUID;
	
	public MessageRemovePlayer() {
	}
	
	public MessageRemovePlayer(UUID uuid) {
		this.playerUUID = uuid;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pBuf = new PacketBuffer(buf);
		pBuf.writeUniqueId(playerUUID);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pBuf = new PacketBuffer(buf);
		playerUUID = pBuf.readUniqueId();
	}
	
	public static class Handler implements IMessageHandler<MessageRemovePlayer, IMessage> {
		@Override
		public IMessage onMessage(MessageRemovePlayer message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> SkinChangingHandler.PLAYER_SKINS.remove(message.playerUUID));
			return null;
		}
	}
}
