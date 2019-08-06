package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by Suffril
 * on 20/01/2019.
 */
public class PlaySFXMessage {
	
	private ResourceLocation sound;
	private UUID playerUUID;
	
	public PlaySFXMessage(ResourceLocation sound, UUID playerUUID) {
		this.playerUUID = playerUUID;
		this.sound = sound;
	}
	
	public static void encode(PlaySFXMessage message, PacketBuffer buffer) {
		buffer.writeResourceLocation(message.sound);
		buffer.writeUniqueId(message.playerUUID);
	}
	
	public static PlaySFXMessage decode(PacketBuffer buffer) {
		return new PlaySFXMessage(buffer.readResourceLocation(), buffer.readUniqueId());
	}
	
	public static class Handler {
		
		public static void handle(PlaySFXMessage message, Supplier<NetworkEvent.Context> ctx) {
            Minecraft.getInstance().deferTask(() -> {
				PlayerEntity player = Minecraft.getInstance().world.getPlayerByUuid(message.playerUUID);
				if (player != null) {
                    RegenCap.get(player).ifPresent((data) -> ClientUtil.playSound(player, message.sound, SoundCategory.PLAYERS, true, () -> !data.getState().equals(PlayerUtil.RegenState.REGENERATING), 1.0F));
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
}
