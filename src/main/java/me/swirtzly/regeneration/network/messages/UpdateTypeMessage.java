package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.types.TypeManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateTypeMessage {

    private String type;

    public UpdateTypeMessage(String type) {
        this.type = type;
    }

    public static void encode(UpdateTypeMessage model, PacketBuffer buf) {
        buf.writeString(model.type);
    }

    public static UpdateTypeMessage decode(PacketBuffer buffer) {
        return new UpdateTypeMessage(buffer.readString(10));
    }

    public static class Handler {
        public static void handle(UpdateTypeMessage message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().getSender().getServer().deferTask(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
                cap.setType(TypeManager.Type.valueOf(message.type));
                cap.synchronise();
            }));
            ctx.get().setPacketHandled(true);
        }
    }
}
