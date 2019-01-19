package me.fril.regeneration.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import me.fril.regeneration.client.skinhandling.SkinChangingHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageRemovePlayer implements IMessage {
	
	private String playerUUID;
	
	public MessageRemovePlayer() {
	}
	
	public MessageRemovePlayer(String uuid) {
		this.playerUUID = uuid;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, playerUUID);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		playerUUID = ByteBufUtils.readUTF8String(buf);
	}
	
	public static class Handler implements IMessageHandler<MessageRemovePlayer, IMessage> {
		@Override
		public IMessage onMessage(MessageRemovePlayer message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> SkinChangingHandler.PLAYER_SKINS.remove(UUID.fromString(message.playerUUID)));
			return null;
		}
	}
}
