package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
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
        type = buffer.readUtf(32767);
    }

    public static void handle(TypeMessage message, Supplier< NetworkEvent.Context > ctx) {
        ctx.get().getSender().getServer().submitAsync(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
            cap.setTransitionType(TransitionTypes.REGISTRY.getValue(new ResourceLocation(message.type)));
            cap.syncToClients(null);
        }));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUtf(this.type);
    }
}