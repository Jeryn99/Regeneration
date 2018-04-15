package com.lcm.regeneration.network;

import com.lcm.regeneration.common.capability.CapabilityRegeneration;
import com.lcm.regeneration.common.capability.IRegeneration;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageChangeStyle implements IMessage {
    private NBTTagCompound style;

    public MessageChangeStyle() {
    }

    public MessageChangeStyle(NBTTagCompound nbtTagCompound) {

    }

    @Override public void fromBytes(ByteBuf buf) {
        this.style = ByteBufUtils.readTag(buf);
    }

    @Override public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.style);
    }

    public static class Handler implements IMessageHandler<MessageChangeStyle, IMessage> {

        @Override public IMessage onMessage(MessageChangeStyle message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                IRegeneration capability = ctx.getServerHandler().player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
                if(capability != null)
                    capability.setStyle(message.style);
            });
            return null;
        }
    }
}

