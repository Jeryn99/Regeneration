package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageTriggerRegeneration {
	
	public MessageTriggerRegeneration() {
	}
	
	public static void encode(MessageTriggerRegeneration msg, PacketBuffer buffer){
	}
	
	public static MessageTriggerRegeneration decode(PacketBuffer buffer){
		return new MessageTriggerRegeneration();
	}
	
	
	public static class Handler{
		
		public static void handle(MessageTriggerRegeneration message, Supplier<NetworkEvent.Context > ctx){
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> {
				EntityPlayerMP player = ctx.get().getSender();
				RegenerationMod.DEBUGGER.getChannelFor(player).out("Regeneration keybind pressed");
				IRegeneration regen = CapabilityRegeneration.getForPlayer(player);
				
				if (!regen.getState().isGraceful()) {
					RegenerationMod.DEBUGGER.getChannelFor(player).warn("Trigger packet was sent when not in a graceful period");
					return;
				}
				regen.triggerRegeneration();
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
}
