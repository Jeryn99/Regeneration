package me.fril.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class MessageRegenChoice implements IMessage {
	
	private boolean enterGrace;
	
	public MessageRegenChoice() {}
	
	public MessageRegenChoice(boolean enterGrace) {
		this.enterGrace = enterGrace;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		enterGrace = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(enterGrace);
	}
	
	
	
	public static class Handler implements IMessageHandler<MessageRegenChoice, IMessage> {
		
		@Override
		public IMessage onMessage(MessageRegenChoice message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().player;
			if (player == null) return null;
			
			IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
			if (message.enterGrace) {
				if (cap.getSolaceTicks() > 0 && cap.getSolaceTicks() < 199 && !cap.isInGracePeriod()) {
					cap.setInGracePeriod(true);
					cap.setSolaceTicks(0);
					cap.setTicksRegenerating(0);
					cap.setGlowing(false);
					cap.sync();
					PlayerUtil.sendMessage(player, "regeneration.messages.grace", true);
				}
			} else {
				if (cap.getSolaceTicks() > 0 && cap.getSolaceTicks() < 18000 && cap.getTicksRegenerating() == 0) {
					cap.setInGracePeriod(false);
					cap.setSolaceTicks(199);
					cap.setGlowing(false);
					cap.setTicksRegenerating(0);
					cap.sync();
				}
			}
			
			return null;
		}
	}
}
