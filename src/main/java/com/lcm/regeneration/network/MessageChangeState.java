package com.lcm.regeneration.network;

import com.lcm.regeneration.common.capability.CapabilityRegeneration;
import com.lcm.regeneration.common.capability.IRegeneration;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessageChangeState implements IMessage {

    private EntityPlayer player;
    private int state;

    public MessageChangeState() { }

    public MessageChangeState(EntityPlayer player, CapabilityRegeneration.RegenerationState state) {
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

    public static class Handler implements IMessageHandler<MessageChangeState, IMessage> {

        @Override public IMessage onMessage(MessageChangeState message, MessageContext ctx) {
            EntityPlayer player = message.player;
            if(!player.hasCapability(CapabilityRegeneration.TIMELORD_CAP, null)) return null;
            IRegeneration handler = player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
            Minecraft.getMinecraft().addScheduledTask(() -> handler.changeState(CapabilityRegeneration.RegenerationState.values()[message.state]));
            return null;
        }
    }
}

