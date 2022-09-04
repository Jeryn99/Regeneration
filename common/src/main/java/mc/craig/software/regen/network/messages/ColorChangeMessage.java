package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ColorChangeMessage {
    private final CompoundTag style;

    public ColorChangeMessage(CompoundTag style) {
        this.style = style;
    }

    public ColorChangeMessage(FriendlyByteBuf buffer) {
        style = buffer.readNbt();
    }

    public static void handle(ColorChangeMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getSender().getServer().submitAsync(() -> {
            RegenerationData.get(ctx.get().getSender()).ifPresent((cap) -> {
                cap.readStyle(message.style);
                cap.syncToClients(null);
            });
            ctx.get().setPacketHandled(true);
        });
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.style);
    }

}
