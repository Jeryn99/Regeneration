package me.sub.regeneration.networking.packets;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import me.sub.regeneration.common.capability.CapabilityRegeneration;
import me.sub.regeneration.common.capability.IRegenerationCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Nictogen on 3/16/18.
 */
public class MessageChangeRegenState implements IMessage {

	private EntityPlayer player;
	private int state;

	public MessageChangeRegenState() { }

    public MessageChangeRegenState(EntityPlayer player, CapabilityRegeneration.RegenerationState state) {
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
            if (!player.hasCapability(CapabilityRegeneration.TIMELORD_CAP, null)) return null;
            IRegenerationCapability handler = player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
            Minecraft.getMinecraft().addScheduledTask(() -> handler.changeState(CapabilityRegeneration.RegenerationState.values()[message.state]));
			return null;
		}
	}
}
