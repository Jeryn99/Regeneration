package me.swirtzly.regen.network.messages;

import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionType;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TypeMessage {

    private final String type;

    public TypeMessage(TransitionType type) {
        this.type = type.getRegistryName().toString();
    }

    public TypeMessage(PacketBuffer buffer) {
        type = buffer.readString(32767);
    }

    public static void handle(TypeMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getSender().getServer().deferTask(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
            cap.setTransitionType(TransitionTypes.REGISTRY.getValue(new ResourceLocation(message.type)));
            cap.syncToClients(null);
        }));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(this.type);
    }
}