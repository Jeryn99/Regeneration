package me.suff.regeneration.network;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.util.ClientUtil;
import me.suff.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by Suffril
 * on 20/01/2019.
 */
public class MessagePlayRegenerationSound {
	
	private ResourceLocation sound;
	private UUID playerUUID;
	
	public MessagePlayRegenerationSound() {
	}
	
	public MessagePlayRegenerationSound(ResourceLocation sound, UUID playerUUID) {
		this.playerUUID = playerUUID;
		this.sound = sound;
	}
	
	public static void encode(MessagePlayRegenerationSound message, PacketBuffer buffer) {
		buffer.writeResourceLocation(message.sound);
		buffer.writeUniqueId(message.playerUUID);
	}
	
	public static MessagePlayRegenerationSound decode(PacketBuffer buffer) {
		return new MessagePlayRegenerationSound(buffer.readResourceLocation(), buffer.readUniqueId());
	}
	
	public static class Handler {
		
		public static void handle(MessagePlayRegenerationSound message, Supplier<NetworkEvent.Context> ctx) {
			Minecraft.getInstance().addScheduledTask(() -> {
				EntityPlayer player = Minecraft.getInstance().world.getPlayerEntityByUUID(message.playerUUID);
				if (player != null) {
					CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> ClientUtil.playSound(player, message.sound, SoundCategory.PLAYERS, true, () -> !data.getState().equals(RegenState.REGENERATING), 1.0F));
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
}
