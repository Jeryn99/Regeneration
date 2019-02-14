package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MessageRemovePlayer {
	
	private UUID playerUUID;
	
	public MessageRemovePlayer() {
	}
	
	public MessageRemovePlayer(UUID uuid) {
		this.playerUUID = uuid;
	}
	
	public static void encode(MessageRemovePlayer messageRemovePlayer, PacketBuffer buffer){
		buffer.writeUniqueId(messageRemovePlayer.playerUUID);
	}
	
	public static MessageRemovePlayer decode(PacketBuffer buffer){
		return new MessageRemovePlayer(buffer.readUniqueId());
	}
	
	public static class Handler {
		public static void handle(MessageRemovePlayer message, Supplier<NetworkEvent.Context> ctx) {
			Minecraft.getInstance().addScheduledTask(() -> SkinChangingHandler.PLAYER_SKINS.remove(message.playerUUID));
		}
	}
}
