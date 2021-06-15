package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.types.RegenTypes;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateTypeMessage {

    private final String type;

    public UpdateTypeMessage(String type) {
        this.type = type;
    }

    public static void encode(UpdateTypeMessage model, PacketBuffer buf) {
        buf.writeUtf(model.type);
    }

    public static UpdateTypeMessage decode(PacketBuffer buffer) {
        return new UpdateTypeMessage(buffer.readUtf(32767));
    }

    public static class Handler {
        public static void handle(UpdateTypeMessage message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().getSender().getServer().submitAsync(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
                cap.setRegenType(RegenTypes.REGISTRY.getValue(new ResourceLocation(message.type)));
                cap.synchronise();
            }));
            ctx.get().setPacketHandled(true);
        }
    }
}
