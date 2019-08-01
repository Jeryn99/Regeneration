package me.swirtzly.regeneration.network;

import me.swirtzly.regeneration.client.skinhandling.SkinInfo;
import me.swirtzly.regeneration.common.capability.RegenCap;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class UpdateSkinMessage {
	
	private boolean isAlex;
	private String encodedSkin;
	
	public UpdateSkinMessage(String pixelData, boolean isAlex) {
		this.isAlex = isAlex;
		encodedSkin = pixelData;
	}
	
	public static void encode(UpdateSkinMessage skin, PacketBuffer buf) {
		buf.writeString(skin.encodedSkin);
		buf.writeBoolean(skin.isAlex);
	}
	
	
	public static UpdateSkinMessage decode(PacketBuffer buf) {
		return new UpdateSkinMessage(buf.readString(32767), buf.readBoolean());
	}
	
	public static class Handler {
		public static void handle(UpdateSkinMessage message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().getSender().getServer().deferTask(() ->
                    RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
						cap.setEncodedSkin(message.encodedSkin);
						if (message.isAlex) {
							cap.setSkinType(SkinInfo.SkinType.ALEX.name());
						} else {
							cap.setSkinType(SkinInfo.SkinType.STEVE.name());
						}
						cap.synchronise();
						NetworkDispatcher.sendPacketToAll(new InvalidatePlayerDataMessage(ctx.get().getSender().getUniqueID()));
					}));
			ctx.get().setPacketHandled(true);
		}
	}
}