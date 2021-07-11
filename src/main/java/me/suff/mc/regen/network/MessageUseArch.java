package me.suff.mc.regen.network;

import io.netty.buffer.ByteBuf;
import me.suff.mc.regen.common.item.arch.ArchHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUseArch implements IMessage {

    public MessageUseArch() {
    }


    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<MessageUseArch, IMessage> {

        @Override
        public IMessage onMessage(MessageUseArch message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP playerIn = ctx.getServerHandler().player;
                ArchHelper.onArchUse(playerIn);
            });
            return null;
        }
    }

}
