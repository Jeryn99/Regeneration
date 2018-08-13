package me.sub.regeneration.networking.packets;

import io.netty.buffer.ByteBuf;
import me.sub.regeneration.common.capabilities.timelord.capability.CapabilityRegeneration;
import me.sub.regeneration.common.capabilities.timelord.capability.IRegenerationCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Nictogen on 3/16/18.
 */
public class MessageRegenerationStyle implements IMessage {
	private NBTTagCompound style;

	public MessageRegenerationStyle() {
	}

	public MessageRegenerationStyle(NBTTagCompound nbtTagCompound) {
		this.style = nbtTagCompound;
	}

	@Override public void fromBytes(ByteBuf buf) {
		this.style = ByteBufUtils.readTag(buf);
	}

	@Override public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.style);
	}

	public static class Handler implements IMessageHandler<MessageRegenerationStyle, IMessage> {

		@Override public IMessage onMessage(MessageRegenerationStyle message, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                IRegenerationCapability capability = ctx.getServerHandler().player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
				if(capability != null)
					capability.setStyle(message.style);
					capability.syncToAll();
					System.out.println(message.style);
			});
			return null;
		}
	}
}
