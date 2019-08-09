package me.swirtzly.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.swirtzly.regeneration.client.skinhandling.SkinInfo;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageUpdateSkin implements IMessage {

    private boolean isAlex;
    private String encodedSkin;

    public MessageUpdateSkin() {
    }

    public MessageUpdateSkin(String pixelData, boolean isAlex) {
        encodedSkin = pixelData;
        this.isAlex = isAlex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        encodedSkin = ByteBufUtils.readUTF8String(buf);
        isAlex = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, encodedSkin);
        buf.writeBoolean(isAlex);
    }

    public static class Handler implements IMessageHandler<MessageUpdateSkin, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateSkin message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
                cap.setEncodedSkin(message.encodedSkin);
                cap.setSkinType(message.isAlex ? SkinInfo.SkinType.ALEX.name() : SkinInfo.SkinType.STEVE.name());
                cap.synchronise();

                NetworkHandler.INSTANCE.sendToAll(new MessageRemovePlayer(player.getUniqueID()));
            });
            return null;
        }
    }
}
