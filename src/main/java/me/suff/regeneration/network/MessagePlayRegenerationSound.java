package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.util.ClientUtil;
import me.suff.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by Suffril
 * on 20/01/2019.
 */
public class MessagePlayRegenerationSound {
	
	private String sound;
	private String playerUUID;
	
	public MessagePlayRegenerationSound() {
	}
	
	public MessagePlayRegenerationSound(SoundEvent sound, String playerUUID) {
		this.playerUUID = playerUUID;
		this.sound = sound.getRegistryName().toString();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		playerUUID = ByteBufUtils.readUTF8String(buf);
		this.sound = ByteBufUtils.readUTF8String(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, playerUUID);
		ByteBufUtils.writeUTF8String(buf, sound);
	}
	
	public static class Handler {
		
		public static void handle(MessagePlayRegenerationSound message, Supplier<NetworkEvent.Context> ctx) {
			Minecraft.getInstance().addScheduledTask(() -> {
				EntityPlayer player = Minecraft.getInstance().world.getPlayerEntityByUUID(UUID.fromString(message.playerUUID));
				if (player != null) {
					IRegeneration data = CapabilityRegeneration.getForPlayer(player);
					ClientUtil.playSound(player, new ResourceLocation(message.sound), SoundCategory.PLAYERS, true, () -> !data.getState().equals(RegenState.REGENERATING), 1.0F);
				}
			});
		}
	}
}
