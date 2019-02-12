package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.RegenConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Maybe a bit overkill, but at least it'll be stable & clear
 */
public class MessageSetPerspective implements IMessage {
	
	private boolean thirdperson, resetPitch;
	
	public MessageSetPerspective() {
	}
	
	public MessageSetPerspective(boolean thirdperson, boolean resetPitch) {
		this.thirdperson = thirdperson;
		this.resetPitch = resetPitch;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		thirdperson = buf.readBoolean();
		resetPitch = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(thirdperson);
		buf.writeBoolean(resetPitch);
	}
	
	public static class Handler implements IMessageHandler<MessageSetPerspective, IMessage> {
		@Override
		public IMessage onMessage(MessageSetPerspective message, MessageContext ctx) {
			Minecraft.getInstance().addScheduledTask(() -> {
				if (message.resetPitch)
					Minecraft.getInstance().player.rotationPitch = 0;
				if (RegenConfig.changePerspective) {
					Minecraft.getInstance().gameSettings.thirdPersonView = message.thirdperson ? 2 : 0;
				}
			});
			return null;
		}
	}
}
