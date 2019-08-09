package me.swirtzly.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageSaveStyle implements IMessage {

    private NBTTagCompound style;

    public MessageSaveStyle() {
    }

    public MessageSaveStyle(NBTTagCompound nbtTagCompound) {
        style = nbtTagCompound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        style = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, style);
    }

    public static class Handler implements IMessageHandler<MessageSaveStyle, IMessage> {
        @Override
        public IMessage onMessage(MessageSaveStyle message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                IRegeneration cap = CapabilityRegeneration.getForPlayer(ctx.getServerHandler().player);
                cap.setStyle(message.style);
                cap.synchronise();
            });
            return null;
        }
    }
}
