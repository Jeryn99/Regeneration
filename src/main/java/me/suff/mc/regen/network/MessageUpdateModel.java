package me.suff.mc.regen.network;

import io.netty.buffer.ByteBuf;
import me.suff.mc.regen.common.capability.CapabilityRegeneration;
import me.suff.mc.regen.common.capability.IRegeneration;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateModel implements IMessage {

    private String preferred;

    public MessageUpdateModel() {
    }

    public MessageUpdateModel(String preferred) {
        this.preferred = preferred;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        preferred = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, preferred);
    }

    public static class Handler implements IMessageHandler<MessageUpdateModel, IMessage> {

        @Override
        public IMessage onMessage(MessageUpdateModel message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                IRegeneration data = CapabilityRegeneration.getForPlayer(ctx.getServerHandler().player);
                data.setPreferredModel(message.preferred);
                data.synchronise();
            });

            return null;
        }
    }
}
