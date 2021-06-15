package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.client.skinhandling.SkinInfo;
import me.suff.mc.regen.common.capability.RegenCap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class NextSkinMessage {

    private boolean isAlex;
    private String encodedSkin;

    public NextSkinMessage(String encodedSkin, boolean isAlex) {
        this.encodedSkin = encodedSkin;
        this.isAlex = isAlex;
    }

    public static void encode(NextSkinMessage message, PacketBuffer packetBuffer) {
        packetBuffer.writeUtf(message.encodedSkin);
        packetBuffer.writeBoolean(message.isAlex);
    }

    public static NextSkinMessage decode(PacketBuffer buffer) {
        return new NextSkinMessage(buffer.readUtf(32767), buffer.readBoolean());
    }

    public static class Handler {
        public static void handle(NextSkinMessage message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().getSender().getServer().submitAsync(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                RegenCap.get(player).ifPresent((data) -> {

                    data.setNextSkin(message.encodedSkin);
                    data.setNextSkinType(message.isAlex ? SkinInfo.SkinType.ALEX : SkinInfo.SkinType.STEVE);
                    data.synchronise();
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
