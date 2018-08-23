package me.sub.regeneration.networking.packets;

import io.netty.buffer.ByteBuf;
import me.sub.regeneration.common.capability.CapabilityRegeneration;
import me.sub.regeneration.common.capability.IRegenerationCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageChangeView implements IMessage {
	
	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		
	}
	
	/**
	 * Deconstruct your message into the supplied byte buffer
	 *
	 * @param buf
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		
	}
	
	public static class Handler implements IMessageHandler<MessageChangeView, IMessage> {
		
		@Override
		public IMessage onMessage(MessageChangeView message, MessageContext ctx) {
			
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			
			if (!player.hasCapability(CapabilityRegeneration.TIMELORD_CAP, null)) return null;
			IRegenerationCapability handler = player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
			
			if (handler.getRegenTicks() > 0 && handler.getRegenTicks() < 200) {
				Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
			}
			
			if (handler.getRegenTicks() > 200) {
				Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
			}
			
			return null;
		}
	}
}
