package me.swirtzly.regeneration.network;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageTriggerForcedRegen {
	
	public MessageTriggerForcedRegen() {
	}

    public static void encode(MessageTriggerForcedRegen msg, PacketBuffer buffer) {
    }

    public static MessageTriggerForcedRegen decode(PacketBuffer buffer) {
        return new MessageTriggerForcedRegen();
    }


    public static class Handler {

        public static void handle(MessageTriggerForcedRegen message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().getSender().getServer().runAsync(() -> CapabilityRegeneration.getForPlayer(ctx.get().getSender()).ifPresent((data) -> {
                if (data.canRegenerate() && data.getState() == PlayerUtil.RegenState.ALIVE) {
                    data.getPlayer().attackEntityFrom(RegenObjects.REGEN_DMG_LINDOS, Integer.MAX_VALUE);
				}
            }));
		}
		
	}
}
