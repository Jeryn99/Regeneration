package com.lcm.regeneration.networking.packets;

import com.lcm.regeneration.common.capabilities.timelord.capability.ITimelordCapability;
import com.lcm.regeneration.common.capabilities.timelord.capability.CapabilityTimelord;
import io.netty.buffer.ByteBuf;
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
				ITimelordCapability capability = ctx.getServerHandler().player.getCapability(CapabilityTimelord.TIMELORD_CAP, null);
				if(capability != null)
					capability.setStyle(message.style);
			});
			return null;
		}
	}
}
