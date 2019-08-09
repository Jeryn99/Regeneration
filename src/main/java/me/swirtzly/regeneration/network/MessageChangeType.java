package me.swirtzly.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.TypeHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageChangeType implements IMessage {

    private String regenType;

    public MessageChangeType() {
    }

    public MessageChangeType(TypeHandler.RegenType type) {
        this.regenType = type.name();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        regenType = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, regenType);
    }


    public static class Handler implements IMessageHandler<MessageChangeType, IMessage> {
        @Override
        public IMessage onMessage(MessageChangeType message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                IRegeneration data = CapabilityRegeneration.getForPlayer(player);
                data.setType(TypeHandler.RegenType.valueOf(message.regenType));
                data.synchronise();
            });
            return null;
        }
    }
}
