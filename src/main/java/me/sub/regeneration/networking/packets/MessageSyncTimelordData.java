package me.sub.regeneration.networking.packets;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import me.sub.regeneration.common.capabilities.timelord.capability.CapabilityRegeneration;
import me.sub.regeneration.common.capabilities.timelord.capability.IRegenerationCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Nictogen on 3/16/18.
 */
public class MessageSyncTimelordData implements IMessage {

	private EntityPlayer player;
	private NBTTagCompound data;

	public MessageSyncTimelordData() { }

	public MessageSyncTimelordData(EntityPlayer player, NBTTagCompound data) {
		this.player = player;
		this.data = data;
	}

	@Override public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
		ByteBufUtils.writeTag(buf, data);
	}

	@Override public void fromBytes(ByteBuf buf) {
		if(Minecraft.getMinecraft().player != null)
			player = Minecraft.getMinecraft().player.world.getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
		if(player != null)
			data = ByteBufUtils.readTag(buf);
	}

	public static class Handler implements IMessageHandler<MessageSyncTimelordData, IMessage> {

		@Override public IMessage onMessage(MessageSyncTimelordData message, MessageContext ctx) {
			EntityPlayer player = message.player;
            if (player == null || !player.hasCapability(CapabilityRegeneration.TIMELORD_CAP, null)) return null;
            IRegenerationCapability handler = player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
			Minecraft.getMinecraft().addScheduledTask(() -> handler.readNBT(message.data));

			return null;
		}
	}
}
