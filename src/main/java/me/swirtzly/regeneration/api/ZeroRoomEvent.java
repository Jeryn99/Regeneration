package me.swirtzly.regeneration.api;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Swirtzly
 * on 20/05/2020 @ 12:21
 */
public class ZeroRoomEvent extends LivingEvent {
    public ZeroRoomEvent(LivingEntity entity) {
        super(entity);
    }
}
