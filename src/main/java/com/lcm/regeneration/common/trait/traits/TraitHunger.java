package com.lcm.regeneration.common.trait.traits;

import com.lcm.regeneration.common.trait.ITrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class TraitHunger implements ITrait {
    @Override
    public String getName() {
        return "Hunger";
    }

    @Override
    public void update(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, Integer.MAX_VALUE, 2));
    }
}
