package me.sub.regeneration.common.events;

import me.sub.regeneration.common.capability.IRegenerationCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by Nictogen on 3/16/18.
 */
public class RegenerationFinishEvent extends PlayerEvent {
	
	private IRegenerationCapability handler;
	
	public RegenerationFinishEvent(EntityPlayer player, IRegenerationCapability handler) {
		super(player);
		this.handler = handler;
	}
	
	public IRegenerationCapability getHandler() {
		return handler;
	}
}
