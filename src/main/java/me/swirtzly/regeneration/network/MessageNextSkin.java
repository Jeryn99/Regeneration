package me.swirtzly.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.swirtzly.regeneration.client.skinhandling.SkinInfo;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageNextSkin implements IMessage {

    private boolean isAlex;
    private String encodedSkin;

    public MessageNextSkin() {
    }

    public MessageNextSkin(String pixelData, boolean isAlex) {
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

    public static class Handler implements IMessageHandler<MessageNextSkin, IMessage> {
        @Override
        public IMessage onMessage(MessageNextSkin message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                IRegeneration data = CapabilityRegeneration.getForPlayer(player);
                data.setNextSkin(message.encodedSkin);
                data.setNextSkinType(message.isAlex ? SkinInfo.SkinType.ALEX : SkinInfo.SkinType.STEVE);
                data.synchronise();
                PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.new_skin"), true);
            });
            return null;
        }
    }
}
