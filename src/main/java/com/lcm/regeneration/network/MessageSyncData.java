package com.lcm.regeneration.network;

import com.lcm.regeneration.common.capability.CapabilityRegeneration;
import com.lcm.regeneration.common.capability.IRegeneration;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessageSyncData implements IMessage {

    private EntityPlayer player;
    private NBTTagCompound data;

    public MessageSyncData() { }

    public MessageSyncData(EntityPlayer player, NBTTagCompound data) {
        this.player = player;
        this.data = data;
    }

    @Override public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
        ByteBufUtils.writeTag(buf, data);
    }

    @Override public void fromBytes(ByteBuf buf) {
        player = Minecraft.getMinecraft().player.world.getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
        data = ByteBufUtils.readTag(buf);
    }

    public static class Handler implements IMessageHandler<MessageSyncData, IMessage> {

        @Override public IMessage onMessage(MessageSyncData message, MessageContext ctx) {
            EntityPlayer player = message.player;
            if(!player.hasCapability(CapabilityRegeneration.TIMELORD_CAP, null)) return null;
            IRegeneration handler = player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
            Minecraft.getMinecraft().addScheduledTask(() -> {
                handler.readNBT(message.data);
            });

            return null;
        }
    }
}

