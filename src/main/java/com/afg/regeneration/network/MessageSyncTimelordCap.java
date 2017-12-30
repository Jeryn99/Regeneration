package com.afg.regeneration.network;

import com.afg.regeneration.capability.TimelordCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by AFlyingGrayson on 12/30/17
 */
public class MessageSyncTimelordCap implements IMessage
{
	public UUID playerUUID;
	public NBTTagCompound nbt;

	public MessageSyncTimelordCap(){}
	public MessageSyncTimelordCap(EntityPlayer player){
		this.playerUUID = player.getPersistentID();
		nbt = (NBTTagCompound) TimelordCapability.TIMELORD_CAP.getStorage().writeNBT(TimelordCapability.TIMELORD_CAP, player.getCapability(TimelordCapability.TIMELORD_CAP, null), null );
	}

	@Override public void fromBytes(ByteBuf buf)
	{
		this.playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
		this.nbt = ByteBufUtils.readTag(buf);
	}

	@Override public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, this.playerUUID.toString());
		ByteBufUtils.writeTag(buf, this.nbt);
	}

	public static class Handler implements IMessageHandler<MessageSyncTimelordCap, IMessage>
	{

		@Override public IMessage onMessage(MessageSyncTimelordCap message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() -> TimelordCapability.TIMELORD_CAP.getStorage().readNBT(TimelordCapability.TIMELORD_CAP, Minecraft.getMinecraft().world.getPlayerEntityByUUID(message.playerUUID).getCapability(TimelordCapability.TIMELORD_CAP, null), null, message.nbt));
			return null;
		}
	}
}
