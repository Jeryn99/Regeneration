package me.fril.regeneration.network;


import io.netty.buffer.ByteBuf;
import me.fril.regeneration.client.skinhandling.SkinChangingHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageTellEveryone implements IMessage {

    public MessageTellEveryone() {
    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<MessageTellEveryone, IMessage> {
        @Override
        public IMessage onMessage(MessageTellEveryone message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> SkinChangingHandler.PLAYER_SKINS.clear());
            return null;
        }
    }
}

