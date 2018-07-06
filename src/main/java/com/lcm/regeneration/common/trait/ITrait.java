package com.lcm.regeneration.common.trait;

import net.minecraft.entity.player.EntityPlayer;

public interface ITrait {

    String getName();

    void update(EntityPlayer player);

    String getMessage();

}
