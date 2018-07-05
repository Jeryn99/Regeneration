package com.lcm.regeneration.common.trait.traits;

import com.lcm.regeneration.common.trait.ITrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class TraitSpeed implements ITrait {

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public void update(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(MobEffects.SPEED, Integer.MAX_VALUE, 2));
    }


}
