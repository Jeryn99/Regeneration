package com.lcm.regeneration.common.capabilities.timelord.events;

import com.lcm.regeneration.common.capabilities.timelord.capability.ITimelordCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by Nictogen on 3/16/18.
 */
public class RegenerationEvent extends PlayerEvent {

    private ITimelordCapability handler;

    public RegenerationEvent(EntityPlayer player, ITimelordCapability handler) {
        super(player);
        this.handler = handler;
    }

    public ITimelordCapability getHandler() {
        return handler;
    }

    public static class RegenerationExplosionEvent extends RegenerationEvent {

        public RegenerationExplosionEvent(EntityPlayer player, ITimelordCapability handler) {
            super(player, handler);
        }
    }

}
