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
	
	private boolean thirdperson, resetPitch;
	
	public MessageSetPerspective() {
	}
	
	public MessageSetPerspective(boolean thirdperson, boolean resetPitch) {
		this.thirdperson = thirdperson;
		this.resetPitch = resetPitch;
	}
	
	public static void encode(MessageSetPerspective messageSetPerspective, PacketBuffer buffer) {
		buffer.writeBoolean(messageSetPerspective.resetPitch);
		buffer.writeBoolean(messageSetPerspective.thirdperson);
	}
	
	public static MessageSetPerspective decode(PacketBuffer buffer) {
		return new MessageSetPerspective(buffer.readBoolean(), buffer.readBoolean());
	}
	
	public static class Handler {
		
		public static void handle(MessageSetPerspective message, Supplier<NetworkEvent.Context> ctx) {
			Minecraft.getInstance().addScheduledTask(() -> {
				if (message.resetPitch)
					Minecraft.getInstance().player.rotationPitch = 0;
				if (RegenConfig.changePerspective) {
					Minecraft.getInstance().gameSettings.thirdPersonView = message.thirdperson ? 2 : 0;
				}
			});
			
		}
	}
}
