package me.suff.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessageTriggerRegeneration implements IMessage {
	
	private EntityPlayer player;
	
	public MessageTriggerRegeneration() {
	}
	
	public MessageTriggerRegeneration(EntityPlayer player) {
		this.player = player;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(player.dimension);
		ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int dim = buf.readInt();
		player = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim).getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
	}
	
	public static class Handler implements IMessageHandler<MessageTriggerRegeneration, IMessage> {
		
		@Override
		public IMessage onMessage(MessageTriggerRegeneration message, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				RegenerationMod.DEBUGGER.getChannelFor(message.player).out("Regeneration keybind pressed");
				IRegeneration regen = CapabilityRegeneration.getForPlayer(message.player);
				
				if (!regen.getState().isGraceful()) {
					RegenerationMod.DEBUGGER.getChannelFor(message.player).warn("Trigger packet was sent when not in a graceful period");
					return;
				}
				regen.triggerRegeneration();
			});
			
			return null;
		}
	}
	
}
