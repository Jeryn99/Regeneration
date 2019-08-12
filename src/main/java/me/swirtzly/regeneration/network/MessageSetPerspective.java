package me.swirtzly.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.swirtzly.regeneration.RegenConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Maybe a bit overkill, but at least it'll be stable & clear
 */
public class MessageSetPerspective implements IMessage {

    private boolean thirdperson, resetPitch;

    public MessageSetPerspective() {
    }

    public MessageSetPerspective(boolean thirdperson, boolean resetPitch) {
        this.thirdperson = thirdperson;
        this.resetPitch = resetPitch;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        thirdperson = buf.readBoolean();
        resetPitch = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(thirdperson);
        buf.writeBoolean(resetPitch);
    }

    public static class Handler implements IMessageHandler<MessageSetPerspective, IMessage> {
        @Override
        public IMessage onMessage(MessageSetPerspective message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if (Minecraft.getMinecraft().getRenderViewEntity().getUniqueID() == Minecraft.getMinecraft().player.getUniqueID()) {
                    if (RegenConfig.changePerspective) {
                        if (message.resetPitch)
                            Minecraft.getMinecraft().player.rotationPitch = 0;
                        Minecraft.getMinecraft().gameSettings.thirdPersonView = message.thirdperson ? 2 : 0;
                    }
                }
            });
            return null;
        }
    }
}
