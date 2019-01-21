package me.fril.regeneration.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.ClientUtil;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Suffril
 * on 20/01/2019.
 */
public class MessagePlayRegenerationSound implements IMessage {
	
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
	
	public static class Handler implements IMessageHandler<MessagePlayRegenerationSound, IMessage> {
		@Override
		public IMessage onMessage(MessagePlayRegenerationSound message, MessageContext ctx) {
			
			Minecraft.getMinecraft().addScheduledTask(() -> {
				EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByUUID(UUID.fromString(message.playerUUID));
				if (player != null) {
					IRegeneration data = CapabilityRegeneration.getForPlayer(player);
					ClientUtil.playSound(player, message.sound, () -> !data.getState().equals(RegenState.REGENERATING), true);
				}
			});
			return null;
		}
	}
}
