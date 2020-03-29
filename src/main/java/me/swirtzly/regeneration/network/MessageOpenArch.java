package me.swirtzly.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.swirtzly.regeneration.RegenerationMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageOpenArch implements IMessage {

    public MessageOpenArch() {
    }


    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<MessageOpenArch, IMessage> {

        @Override
        public IMessage onMessage(MessageOpenArch message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP playerIn = ctx.getServerHandler().player;
                playerIn.openGui(RegenerationMod.INSTANCE, 99, playerIn.world, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            });
            return null;
        }
    }

}
