package me.swirtzly.regeneration.compat.lucraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * Created by Nictogen on 2019-02-18.
 * <p>
 * PlayerCanRegen is fired when it is checked if the player can regenerate.
 * It's used in the LC compat module to disable regenerating while you have a superpower.
 */

@Cancelable
public class PlayerCanRegenEvent extends PlayerEvent {
    public PlayerCanRegenEvent(EntityPlayer player) {
        super(player);
    }
}
