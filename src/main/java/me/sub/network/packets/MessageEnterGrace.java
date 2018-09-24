package me.sub.network.packets;

import io.netty.buffer.ByteBuf;
import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class MessageEnterGrace implements IMessage {

    private boolean stopRegen;

    public MessageEnterGrace() {
    }

    public MessageEnterGrace(boolean stopRegen) {
        this.stopRegen = stopRegen;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stopRegen = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(stopRegen);
    }

    public static class Handler implements IMessageHandler<MessageEnterGrace, IMessage> {

        @Override
        public IMessage onMessage(MessageEnterGrace message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            if (player == null || !player.hasCapability(CapabilityRegeneration.CAPABILITY, null)) return null;

            IRegeneration regenInfo = CapabilityRegeneration.get(player);

            if (message.stopRegen) {
                if (regenInfo.getSolaceTicks() > 0 && regenInfo.getSolaceTicks() < 199 && !regenInfo.isInGracePeriod()) {
                    regenInfo.setInGracePeriod(true);
                    regenInfo.setSolaceTicks(0);
                    regenInfo.setTicksRegenerating(0);
                    regenInfo.sync();
                    player.sendStatusMessage(new TextComponentString("You have entered a grace period of 15 minutes"), true);
                }
            } else {
                if (regenInfo.getSolaceTicks() > 0 && regenInfo.getSolaceTicks() < 18000) {
                    regenInfo.setInGracePeriod(false);
                    regenInfo.setSolaceTicks(199);
                    regenInfo.sync();
                }
            }
            return null;
        }
    }
}
