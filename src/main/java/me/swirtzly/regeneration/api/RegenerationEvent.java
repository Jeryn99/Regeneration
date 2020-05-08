package me.swirtzly.regeneration.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class RegenerationEvent extends EntityEvent {

    public RegenerationEvent(Entity entity) {
        super(entity);
    }
}
