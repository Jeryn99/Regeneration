package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.client.skinhandling.SkinInfo;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.naming.Context;
import java.util.function.Supplier;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageUpdateSkin {
	
	private boolean isAlex;
	private byte[] encodedSkin;
	
	public MessageUpdateSkin() {
	}
	
	public MessageUpdateSkin(byte[] pixelData, boolean isAlex) {
		encodedSkin = pixelData;
		this.isAlex = isAlex;
	}
	
	public static void encode(MessageUpdateSkin skin, ByteBuf buf) {
		buf.writeInt(skin.encodedSkin.length);
		for (int i = 0; i < skin.encodedSkin.length; i++) {
			buf.writeByte(skin.encodedSkin[i]);
		}
		buf.writeBoolean(skin.isAlex);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int length = buf.readInt();
		this.encodedSkin = new byte[length];
		for (int i = 0; i < length; i++) {
			this.encodedSkin[i] = buf.readByte();
		}
		
		isAlex = buf.readBoolean();
	}
	
	public static class Handler {
		public static void handle(MessageUpdateSkin message, Supplier<NetworkEvent.Context> ctx){
			ctx.get().getSender().getServerWorld().addScheduledTask(() -> {
				EntityPlayerMP player = ctx.get().getSender();
				IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
				cap.setEncodedSkin(message.encodedSkin);
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
