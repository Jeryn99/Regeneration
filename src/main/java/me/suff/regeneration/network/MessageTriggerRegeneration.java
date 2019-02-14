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
	
	private UUID player;
	private int dim;
	
	public MessageTriggerRegeneration() {
	}
	
	public MessageTriggerRegeneration(UUID player, int dim) {
		this.player = player;
		this.dim = dim;
	}
	
	public static void encode(MessageTriggerRegeneration msg, PacketBuffer buffer){
		buffer.writeUniqueId(msg.player);
		buffer.writeInt(msg.dim);
	}
	
	public static MessageTriggerRegeneration decode(PacketBuffer buffer){
		return new MessageTriggerRegeneration(buffer.readUniqueId(), buffer.readInt());
	}
	
	
	public static class Handler{
		
		public static void handle(MessageTriggerRegeneration message, Supplier<NetworkEvent.Context > ctx){
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> {
				EntityPlayerMP player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(message.player);
				RegenerationMod.DEBUGGER.getChannelFor(player).out("Regeneration keybind pressed");
				IRegeneration regen = CapabilityRegeneration.getForPlayer(player);
				
				if (!regen.getState().isGraceful()) {
					RegenerationMod.DEBUGGER.getChannelFor(player).warn("Trigger packet was sent when not in a graceful period");
					return;
				}
				regen.triggerRegeneration();
			});
		}
	}
	
}
