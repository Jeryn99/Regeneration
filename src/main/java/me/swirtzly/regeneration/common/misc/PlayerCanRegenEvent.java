package me.swirtzly.regeneration.common.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerCanRegenEvent extends PlayerEvent {

    public PlayerCanRegenEvent(PlayerEntity player) {
        super(player);
    }
}
