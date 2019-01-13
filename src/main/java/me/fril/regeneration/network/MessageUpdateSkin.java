package me.fril.regeneration.network;


import io.netty.buffer.ByteBuf;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.PlayerUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageUpdateSkin implements IMessage {

    private String encodedSkin;

    public MessageUpdateSkin() {
    }

    public MessageUpdateSkin(String string) {
        encodedSkin = string;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        encodedSkin = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, encodedSkin);
    }

    public static class Handler implements IMessageHandler<MessageUpdateSkin, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateSkin message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                IRegeneration cap = CapabilityRegeneration.getForPlayer(ctx.getServerHandler().player);
                String uuid = cap.getPlayer().getUniqueID().toString();
                cap.setEncodedSkin(message.encodedSkin);
                cap.setSkinLoaded(true);
                cap.synchronise();
                PlayerUtil.sendPacketToAll(new MessageTellEveryone(uuid));
            });
            return null;
        }
    }
}

