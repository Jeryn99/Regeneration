package me.fril.regeneration.network;


import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import io.netty.buffer.ByteBuf;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.client.SkinChangingHandler;
import me.fril.regeneration.client.rendering.LayerRegeneration;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageTellEveryone implements IMessage {

    public MessageTellEveryone() {
    }

    private EntityPlayer player;

    public MessageTellEveryone(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        //player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
    }

    public static class Handler implements IMessageHandler<MessageTellEveryone, IMessage> {
        @Override
        public IMessage onMessage(MessageTellEveryone message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                // if(message.player == null) return;
                // if (LayerRegeneration.PLAYER_SKINS.containsKey(message.player.getUniqueID())) {
                LayerRegeneration.PLAYER_SKINS.clear();
                //  }
            });
            return null;
        }
    }
}

