package me.fril.regeneration.network;


import io.netty.buffer.ByteBuf;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageUpdateSkin implements IMessage {

    private byte[] encodedSkin;

    public MessageUpdateSkin() {
    }

    public MessageUpdateSkin(byte[] skin) {
        encodedSkin = skin;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        this.encodedSkin = new byte[length];
        for (int i = 0; i < length; i++) {
            this.encodedSkin[i] = buf.readByte();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.encodedSkin.length);
        for (int i = 0; i < this.encodedSkin.length; i++) {
            buf.writeByte(this.encodedSkin[i]);
        }
    }

    public static class Handler implements IMessageHandler<MessageUpdateSkin, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateSkin message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
                cap.setEncodedSkin(message.encodedSkin);
                cap.synchronise();
                NetworkHandler.INSTANCE.sendToAll(new MessageTellEveryone());
            });
            return null;
        }
    }
}

