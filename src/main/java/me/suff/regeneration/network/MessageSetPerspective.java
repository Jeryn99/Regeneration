package me.suff.regeneration.network;

import me.suff.regeneration.RegenConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Maybe a bit overkill, but at least it'll be stable & clear
 */
public class MessageSetPerspective {
	
	private boolean thirdPerson;
	
	public MessageSetPerspective(boolean thirdPerson) {
		this.thirdPerson = thirdPerson;
	}
	
	public static void encode(MessageSetPerspective messageSetPerspective, PacketBuffer buffer) {
		buffer.writeBoolean(messageSetPerspective.thirdPerson);
	}
	
	public static MessageSetPerspective decode(PacketBuffer buffer) {
		return new MessageSetPerspective(buffer.readBoolean());
	}
	
	public static class Handler {
		
		public static void handle(MessageSetPerspective message, Supplier<NetworkEvent.Context> ctx) {
			Minecraft.getInstance().addScheduledTask(() -> {
				if (RegenConfig.CLIENT.changePerspective.get()) {
					Minecraft.getInstance().gameSettings.thirdPersonView = message.thirdPerson ? 0 : 2;
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
