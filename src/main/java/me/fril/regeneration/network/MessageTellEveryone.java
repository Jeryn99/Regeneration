package me.fril.regeneration.network;


import io.netty.buffer.ByteBuf;
import me.fril.regeneration.client.SkinChangingHandler;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageTellEveryone implements IMessage {

    public MessageTellEveryone() {
    }

    public MessageTellEveryone(String string) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<MessageTellEveryone, IMessage> {
        @Override
        public IMessage onMessage(MessageTellEveryone message, MessageContext ctx) {
            FMLClientHandler.instance().getClient().world.playerEntities.forEach(player -> {
                String skin = CapabilityRegeneration.getForPlayer(player).getEncodedSkin();
                try {
                    SkinChangingHandler.cacheImage((AbstractClientPlayer) player, skin);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return null;
        }
    }
}

