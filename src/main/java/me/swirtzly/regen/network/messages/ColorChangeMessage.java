package me.swirtzly.regen.network.messages;

import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ColorChangeMessage {
    private CompoundNBT style;

    public ColorChangeMessage(CompoundNBT style) {
        this.style = style;
    }

    public ColorChangeMessage(PacketBuffer buffer) {
        style = buffer.readCompoundTag();
    }

    public static void handle(ColorChangeMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getSender().getServer().deferTask(() -> {
            RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
                cap.readStyle(message.style);
                cap.syncToClients(null);
            });
            ctx.get().setPacketHandled(true);
        });
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeCompoundTag(this.style);
    }

}
