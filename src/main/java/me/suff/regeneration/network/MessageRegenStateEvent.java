package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.handlers.ActingForwarder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessageRegenStateEvent implements IMessage {
	
	private EntityPlayer player;
	private String event;
	
	public MessageRegenStateEvent() {
	}
	
	public MessageRegenStateEvent(EntityPlayer player, String event) {
		this.player = player;
		this.event = event;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
		ByteBufUtils.writeUTF8String(buf, event);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (Minecraft.getInstance().player == null)
			return;
		player = Minecraft.getInstance().player.world.getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
		event = ByteBufUtils.readUTF8String(buf);
	}
	
	public static class Handler implements IMessageHandler<MessageRegenStateEvent, IMessage> {
		
		@Override
		public IMessage onMessage(MessageRegenStateEvent message, MessageContext ctx) {
			Minecraft.getInstance().addScheduledTask(() -> ActingForwarder.onClient(message.event, CapabilityRegeneration.getForPlayer(message.player)));
			return null;
		}
	}
	
}
