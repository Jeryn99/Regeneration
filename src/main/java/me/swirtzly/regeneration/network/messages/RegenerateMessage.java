package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.RegenCap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class RegenerateMessage {

    public static void encode(RegenerateMessage msg, PacketBuffer buffer) {
    }

    public static RegenerateMessage decode(PacketBuffer buffer) {
        return new RegenerateMessage();
    }


    public static class Handler {

        public static void handle(RegenerateMessage message, Supplier<NetworkEvent.Context> ctx) {
            Objects.requireNonNull(ctx.get().getSender()).getServer().deferTask(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                RegenerationMod.LOG.warn("Regeneration keybind pressed");
                if (player != null) {
                    RegenCap.get(player).ifPresent((cap) -> {
                        if (!cap.getState().isGraceful()) {
                            RegenerationMod.LOG.warn("Trigger packet was sent when not in a graceful period");
                            return;
                        }
                        cap.triggerRegeneration();
                    });
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
