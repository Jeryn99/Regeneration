package me.fril.regeneration.network;


import io.netty.buffer.ByteBuf;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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

    private String encodedSkin, uuid;

    public MessageUpdateSkin() {
    }

    public MessageUpdateSkin(String skin, String playerUUID) {
        encodedSkin = skin;
        uuid = playerUUID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        encodedSkin = ByteBufUtils.readUTF8String(buf);
        uuid = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, encodedSkin);
        ByteBufUtils.writeUTF8String(buf, uuid);
    }

    public static class Handler implements IMessageHandler<MessageUpdateSkin, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateSkin message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(UUID.fromString(message.uuid));
                IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
                cap.setEncodedSkin(message.encodedSkin);
                cap.synchronise();
                PlayerUtil.sendPacketToAll(new MessageTellEveryone(message.uuid));
            });
            return null;
        }
    }
}

