package com.lcm.regeneration.common.trait.traits;

import com.lcm.regeneration.common.trait.ITrait;

import net.minecraft.entity.player.EntityPlayer;

public class TraitNone implements ITrait {
    @Override
    public String getName() {
        return "BLANK";
    }

    @Override
    public void update(EntityPlayer player) {

    }

    @Override
    public String getMessage() {
        return "trait.messages.no_trait";
    }
}
