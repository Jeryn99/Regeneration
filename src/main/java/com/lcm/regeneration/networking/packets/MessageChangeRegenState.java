package com.lcm.regeneration.networking.packets;

import com.lcm.regeneration.common.capabilities.timelord.capability.ITimelordCapability;
import com.lcm.regeneration.common.capabilities.timelord.capability.CapabilityTimelord;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by Nictogen on 3/16/18.
 */
public class MessageChangeRegenState implements IMessage {

	private EntityPlayer player;
	private int state;

	public MessageChangeRegenState() { }

	public MessageChangeRegenState(EntityPlayer player, CapabilityTimelord.RegenerationState state) {
		this.player = player;
		this.state = state.ordinal();
	}

	@Override public void toBytes(ByteBuf buf) {
		buf.writeInt(state);
		ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
	}

	@Override public void fromBytes(ByteBuf buf) {
		state = buf.readInt();
		player = Minecraft.getMinecraft().player.world.getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
	}

	public static class Handler implements IMessageHandler<MessageChangeRegenState, IMessage> {

		@Override public IMessage onMessage(MessageChangeRegenState message, MessageContext ctx) {
			EntityPlayer player = message.player;
			if(!player.hasCapability(CapabilityTimelord.TIMELORD_CAP, null)) return null;
			ITimelordCapability handler = player.getCapability(CapabilityTimelord.TIMELORD_CAP, null);
			Minecraft.getMinecraft().addScheduledTask(() -> handler.changeState(CapabilityTimelord.RegenerationState.values()[message.state]));
			return null;
		}
	}
}
