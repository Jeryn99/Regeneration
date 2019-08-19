package me.swirtzly.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessageTriggerRegeneration implements IMessage {

    private EntityPlayer player;

    public MessageTriggerRegeneration() {
    }

    public MessageTriggerRegeneration(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(player.dimension);
        ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int dim = buf.readInt();
        player = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim).getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
    }

    public static class Handler implements IMessageHandler<MessageTriggerRegeneration, IMessage> {

        @Override
        public IMessage onMessage(MessageTriggerRegeneration message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                IRegeneration regen = CapabilityRegeneration.getForPlayer(message.player);
                if (regen.getState().isGraceful()) {
                    regen.triggerRegeneration();
                }
            });

            return null;
        }
    }

}
