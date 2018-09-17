package me.sub.network.packets;

import io.netty.buffer.ByteBuf;
import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class MessageEnterGrace implements IMessage {

    private EntityPlayer player;

    public MessageEnterGrace() {
    }

    public MessageEnterGrace(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (Minecraft.getMinecraft().player != null)
            player = Minecraft.getMinecraft().player.world.getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
    }

    public static class Handler implements IMessageHandler<MessageEnterGrace, IMessage> {

        @Override
        public IMessage onMessage(MessageEnterGrace message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            if (player == null || !player.hasCapability(CapabilityRegeneration.CAPABILITY, null)) return null;

            IRegeneration regenInfo = CapabilityRegeneration.get(player);
            if (regenInfo.getSolaceTicks() < 199) {
                regenInfo.setInGracePeriod(true);
                regenInfo.setSolaceTicks(0);
                regenInfo.setTicksRegenerating(0);
                regenInfo.sync();
                player.sendStatusMessage(new TextComponentString("You have entered a grace period of 15 minutes"), true);
            }

            return null;
        }
    }
}
