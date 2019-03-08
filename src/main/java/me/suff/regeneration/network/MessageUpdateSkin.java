package me.suff.regeneration.network;

import me.suff.regeneration.client.skinhandling.SkinInfo;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageUpdateSkin {
	
	private boolean isAlex;
	private String encodedSkin;
	
	public MessageUpdateSkin(String pixelData, boolean isAlex) {
		this.isAlex = isAlex;
		encodedSkin = pixelData;
	}
	
	public static void encode(MessageUpdateSkin skin, PacketBuffer buf) {
		buf.writeString(skin.encodedSkin);
		buf.writeBoolean(skin.isAlex);
	}
	
	
	public static MessageUpdateSkin decode(PacketBuffer buf) {
		return new MessageUpdateSkin(buf.readString(32767), buf.readBoolean());
	}
	
	public static class Handler {
		public static void handle(MessageUpdateSkin message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().getSender().getServerWorld().addScheduledTask(() ->
					CapabilityRegeneration.getForPlayer(ctx.get().getSender()).ifPresent((cap) -> {
						cap.setEncodedSkin(message.encodedSkin);
						System.out.println("SERVER SKIN " + cap.getEncodedSkin());
						if (message.isAlex) {
							cap.setSkinType(SkinInfo.SkinType.ALEX.name());
						} else {
							cap.setSkinType(SkinInfo.SkinType.STEVE.name());
						}
						cap.sync();
						
						NetworkHandler.sendPacketToAll(new MessageRemovePlayer(ctx.get().getSender().getUniqueID()));
					}));
			ctx.get().setPacketHandled(true);
		}
	}
}