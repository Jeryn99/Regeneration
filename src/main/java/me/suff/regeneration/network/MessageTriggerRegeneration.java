package me.suff.regeneration.network;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageTriggerRegeneration {
	
	public MessageTriggerRegeneration() {
	}
	
	public static void encode(MessageTriggerRegeneration msg, PacketBuffer buffer) {
	}
	
	public static MessageTriggerRegeneration decode(PacketBuffer buffer) {
		return new MessageTriggerRegeneration();
	}
	
	
	public static class Handler {
		
		public static void handle(MessageTriggerRegeneration message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> {
				EntityPlayerMP player = ctx.get().getSender();
				RegenerationMod.DEBUGGER.getChannelFor(player).out("Regeneration keybind pressed");
				CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
					if (!cap.getState().isGraceful()) {
						RegenerationMod.DEBUGGER.getChannelFor(player).warn("Trigger packet was sent when not in a graceful period");
						return;
					}
					cap.triggerRegeneration();
				});
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
}
