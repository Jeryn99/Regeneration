package me.swirtzly.regeneration.network;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class MessageTriggerRegeneration {

    public static void encode(MessageTriggerRegeneration msg, PacketBuffer buffer) {
    }

    public static MessageTriggerRegeneration decode(PacketBuffer buffer) {
        return new MessageTriggerRegeneration();
    }


    public static class Handler {

        public static void handle(MessageTriggerRegeneration message, Supplier<NetworkEvent.Context> ctx) {
            Objects.requireNonNull(ctx.get().getSender()).getServer().runAsync(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                RegenerationMod.LOG.warn("Regeneration keybind pressed");
                if (player != null) {
                    CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
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
