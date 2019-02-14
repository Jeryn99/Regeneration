package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.suff.regeneration.client.skinhandling.SkinInfo;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.naming.Context;
import java.util.function.Supplier;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageUpdateSkin {
	
	private boolean isAlex;
	private PacketBuffer encodedSkin;
	
	public MessageUpdateSkin() {
	}
	
	public MessageUpdateSkin(PacketBuffer pixelData, boolean isAlex) {
		encodedSkin = pixelData;
		this.isAlex = isAlex;
	}
	
	public static void encode(MessageUpdateSkin skin, PacketBuffer buf) {
		buf.writeByteArray(skin.encodedSkin.readByteArray());
		buf.writeBoolean(skin.isAlex);
	}
	

	public static MessageUpdateSkin decode(PacketBuffer buf) {
		return new MessageUpdateSkin(new PacketBuffer(Unpooled.wrappedBuffer(buf.readByteArray(32600))), buf.readBoolean());
	}
	
	public static class Handler {
		public static void handle(MessageUpdateSkin message, Supplier<NetworkEvent.Context> ctx){
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> {
				EntityPlayerMP player = ctx.get().getSender();
				IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
				cap.setEncodedSkin(message.encodedSkin.readByteArray());
				if (message.isAlex) {
					cap.setSkinType(SkinInfo.SkinType.ALEX.name());
				} else {
					cap.setSkinType(SkinInfo.SkinType.STEVE.name());
				}
				cap.synchronise();
				
				NetworkHandler.sendPacketToAll(new MessageRemovePlayer(player.getUniqueID()));
			});
		}
	}
}
