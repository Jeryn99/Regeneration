package me.swirtzly.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.RegenState;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTriggerForcedRegen implements IMessage {
	
	public MessageTriggerForcedRegen() {
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
	
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
	
	}
	
	public static class Handler implements IMessageHandler<MessageTriggerForcedRegen, IMessage> {
		
		@Override
		public IMessage onMessage(MessageTriggerForcedRegen message, MessageContext ctx) {
			
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				IRegeneration cap = CapabilityRegeneration.getForPlayer(ctx.getServerHandler().player);
				if (cap.canRegenerate() && cap.getState() == RegenState.ALIVE) {
					cap.getPlayer().attackEntityFrom(RegenObjects.REGEN_DMG_LINDOS, Integer.MAX_VALUE);
				}
			});
			
			return null;
		}
		
	}
}
