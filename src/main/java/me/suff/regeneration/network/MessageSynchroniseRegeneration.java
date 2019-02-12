package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class MessageSynchroniseRegeneration {
	
	private EntityPlayer player;
	private NBTTagCompound data;
	
	
	public MessageSynchroniseRegeneration() {
	}
	
	public MessageSynchroniseRegeneration(EntityPlayer player, NBTTagCompound data) {
		this.player = player;
		this.data = data;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
		ByteBufUtils.writeTag(buf, data);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (Minecraft.getInstance().player == null)
			return;
		player = Minecraft.getInstance().player.world.getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
		data = ByteBufUtils.readTag(buf);
	}
	
	public static class Handler {
		public static void handle(MessageSynchroniseRegeneration message, Supplier<NetworkEvent.Context> ctx) {
			EntityPlayer player = message.player;
			if (player != null)
				Minecraft.getInstance().addScheduledTask(() -> CapabilityRegeneration.getForPlayer(player).deserializeNBT(message.data));
		}
	}
	
}
