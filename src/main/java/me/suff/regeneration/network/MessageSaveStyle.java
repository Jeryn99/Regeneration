package me.suff.regeneration.network;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageSaveStyle {
	
	private NBTTagCompound style;
	
	public MessageSaveStyle() {
	}
	
	public MessageSaveStyle(NBTTagCompound nbtTagCompound) {
		style = nbtTagCompound;
	}
	
	public static void encode(MessageSaveStyle saveStyle, PacketBuffer buf) {
		buf.writeCompoundTag(saveStyle.style);
	}
	
	public static MessageSaveStyle decode(PacketBuffer buffer) {
		return new MessageSaveStyle(buffer.readCompoundTag());
	}
	
	public static class Handler {
		public static void handle(MessageSaveStyle message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> {
				CapabilityRegeneration.getForPlayer(ctx.get().getSender()).ifPresent((cap) -> {
					cap.setStyle(message.style);
					cap.sync();
				});
				ctx.get().setPacketHandled(true);
			});
		}
	}
}
