package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.capability.RegenCap;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateSkinMapMessage {

    private final String preferred;

    public UpdateSkinMapMessage(String preferred) {
        this.preferred = preferred;
    }

    public static void encode(UpdateSkinMapMessage model, PacketBuffer buf) {
        buf.writeUtf(model.preferred);
    }

    public static UpdateSkinMapMessage decode(PacketBuffer buffer) {
        return new UpdateSkinMapMessage(buffer.readUtf(32767));
    }

    public static class Handler {
        public static void handle(UpdateSkinMapMessage message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().getSender().getServer().submitAsync(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
                cap.setPreferredModel(message.preferred);
                cap.synchronise();
            }));
            ctx.get().setPacketHandled(true);
        }
    }
}