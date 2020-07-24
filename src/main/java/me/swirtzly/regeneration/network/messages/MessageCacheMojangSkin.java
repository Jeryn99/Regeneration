package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageCacheMojangSkin {

    private final UUID player;
    private final boolean remove;

    public MessageCacheMojangSkin(UUID player, boolean remove) {
        this.player = player;
        this.remove = remove;
    }

    public static void encode(MessageCacheMojangSkin message, PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(message.player);
        packetBuffer.writeBoolean(message.remove);
    }

    public static MessageCacheMojangSkin decode(PacketBuffer buffer) {
        return new MessageCacheMojangSkin(buffer.readUniqueId(), buffer.readBoolean());
    }

    public static class Handler {
        public static void handle(MessageCacheMojangSkin message, Supplier<NetworkEvent.Context> ctx) {
            Minecraft.getInstance().deferTask(() -> {
                if (message.remove) {
                    SkinManipulation.MOJANG.remove(message.player);
                } else {
                    SkinManipulation.createSkin(message.player);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
