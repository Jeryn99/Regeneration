package me.craig.software.regen.network.messages;

import me.craig.software.regen.common.regen.IRegen;
import me.craig.software.regen.common.regen.RegenCap;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/* Created by Craig on 29/01/2021 */
public class ChangeSoundScheme {

    private final String type;

    public ChangeSoundScheme(IRegen.TimelordSound type) {
        this.type = type.name();
    }

    public ChangeSoundScheme(PacketBuffer buffer) {
        type = buffer.readUtf(32767);
    }

    public static void handle(ChangeSoundScheme message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getSender().getServer().submitAsync(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
            cap.setTimelordSound(IRegen.TimelordSound.valueOf(message.type));
            cap.syncToClients(null);
        }));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUtf(this.type);
    }
}