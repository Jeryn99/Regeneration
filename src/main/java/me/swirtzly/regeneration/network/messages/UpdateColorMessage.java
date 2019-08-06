package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.common.capability.RegenCap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class UpdateColorMessage {
	
	private CompoundNBT style;
	
	public UpdateColorMessage(CompoundNBT nbtTagCompound) {
		style = nbtTagCompound;
	}
	
	public static void encode(UpdateColorMessage saveStyle, PacketBuffer buf) {
		buf.writeCompoundTag(saveStyle.style);
	}
	
	public static UpdateColorMessage decode(PacketBuffer buffer) {
		return new UpdateColorMessage(buffer.readCompoundTag());
	}
	
	public static class Handler {
		public static void handle(UpdateColorMessage message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().getSender().getServer().deferTask(() -> {
				RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
					cap.setStyle(message.style);
					cap.synchronise();
				});
				ctx.get().setPacketHandled(true);
			});
		}
	}
}
