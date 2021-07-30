package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ModelMessage {

    private final String type;

    public ModelMessage(PlayerUtil.SkinType type) {
        this.type = type.name();
    }

    public ModelMessage(FriendlyByteBuf buffer) {
        type = buffer.readUtf(32767);
    }

    public static void handle(ModelMessage message, Supplier<NetworkEvent.Context> ctx) {
        Objects.requireNonNull(ctx.get().getSender()).getServer().submitAsync(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
            cap.setPreferredModel(PlayerUtil.SkinType.valueOf(message.type));
            cap.syncToClients(null);
        }));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.type);
    }
}