package com.lcm.regeneration.regeneration_events;

import com.lcm.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class RegenerationEvent extends PlayerEvent {

    private IRegeneration handler;

    public RegenerationEvent(EntityPlayer player, IRegeneration handler) {
        super(player);
        this.handler = handler;
    }

    public IRegeneration getHandler() {
        return handler;
    }

    public static class RegenerationExplosionEvent extends RegenerationEvent{

        public RegenerationExplosionEvent(EntityPlayer player, IRegeneration handler) {
            super(player, handler);
        }
    }

}

