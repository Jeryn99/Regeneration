package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ForceRegenerationMessage {

    public static void encode(ForceRegenerationMessage event, PacketBuffer packetBuffer) {

    }

    public static ForceRegenerationMessage decode(PacketBuffer buffer) {
        return new ForceRegenerationMessage();
    }

    public static class Handler {
        public static void handle(ForceRegenerationMessage message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().getSender().getServer().submitAsync(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                RegenCap.get(player).ifPresent((data) -> {
                    if (data.getState() == PlayerUtil.RegenState.ALIVE && data.getRegenerationsLeft() > 0) {
                        player.hurt(RegenObjects.REGEN_DMG_FORCED, Integer.MAX_VALUE);
                    }
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
