package com.lcm.regeneration.common.trait.traits;

import com.lcm.regeneration.common.trait.ITrait;
import net.minecraft.entity.player.EntityPlayer;

public class TraitHunger implements ITrait {
    @Override
    public String getName() {
        return "Hunger";
    }

    @Override
    public void update(EntityPlayer player) {
        //  player.addExhaustion(11111111111F);
    }

    @Override
    public String getMessage() {
        return "trait.messages.hunger";
    }
}
