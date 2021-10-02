package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.capability.RegenCap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by Craig on 20/09/2018.
 */
public class UpdateColorMessage {

    private CompoundNBT style;

    public UpdateColorMessage(CompoundNBT nbtTagCompound) {
        style = nbtTagCompound;
    }

    public static void encode(UpdateColorMessage saveStyle, PacketBuffer buf) {
        buf.writeNbt(saveStyle.style);
    }

    public static UpdateColorMessage decode(PacketBuffer buffer) {
        return new UpdateColorMessage(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(UpdateColorMessage message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().getSender().getServer().submitAsync(() -> {
                RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
                    cap.setStyle(message.style);
                    cap.synchronise();
                });
                ctx.get().setPacketHandled(true);
            });
        }
    }
}