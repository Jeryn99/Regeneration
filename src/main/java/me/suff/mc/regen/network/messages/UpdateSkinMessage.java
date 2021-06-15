package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.client.skinhandling.SkinInfo;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.network.NetworkDispatcher;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by Craig on 20/09/2018.
 */
public class UpdateSkinMessage {

    private final boolean isAlex;
    private final String encodedSkin;

    public UpdateSkinMessage(String pixelData, boolean isAlex) {
        this.isAlex = isAlex;
        encodedSkin = pixelData;
    }

    public static void encode(UpdateSkinMessage skin, PacketBuffer buf) {
        buf.writeUtf(skin.encodedSkin);
        buf.writeBoolean(skin.isAlex);
    }

    public static UpdateSkinMessage decode(PacketBuffer buf) {
        return new UpdateSkinMessage(buf.readUtf(32767), buf.readBoolean());
    }

    public static class Handler {
        public static void handle(UpdateSkinMessage message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().getSender().getServer().submitAsync(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
                cap.setEncodedSkin(message.encodedSkin);
                cap.setSkinType(message.isAlex ? SkinInfo.SkinType.ALEX.name() : SkinInfo.SkinType.STEVE.name());
                cap.synchronise();
                NetworkDispatcher.sendPacketToAll(new InvalidatePlayerDataMessage(ctx.get().getSender().getUUID()));
            }));
            ctx.get().setPacketHandled(true);
        }
    }
}
