package me.craig.software.regen.network.messages;

import me.craig.software.regen.util.PlayerUtil;
import me.craig.software.regen.common.regen.RegenCap;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ModelMessage {

    private final String type;

    public ModelMessage(PlayerUtil.SkinType type) {
        this.type = type.name();
    }

    public ModelMessage(PacketBuffer buffer) {
        type = buffer.readUtf(32767);
    }

    public static void handle(ModelMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getSender().getServer().submitAsync(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
            cap.setPreferredModel(PlayerUtil.SkinType.valueOf(message.type));
            cap.syncToClients(null);
        }));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUtf(this.type);
    }
}