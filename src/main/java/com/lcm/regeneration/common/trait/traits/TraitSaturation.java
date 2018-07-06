package com.lcm.regeneration.common.trait.traits;

import com.lcm.regeneration.common.trait.ITrait;
import net.minecraft.entity.player.EntityPlayer;

public class TraitSaturation implements ITrait {
    @Override
    public String getName() {
        return "Saturation";
    }

    @Override
    public void update(EntityPlayer player) {
        if (!player.world.isRemote) {
            player.getFoodStats().addStats(3, 1.0F);
        }
    }

    @Override
    public String getMessage() {
        return "trait.messages.large_stomach";
    }
}
