package me.sub.regeneration.common.trait.traits;

import me.sub.regeneration.common.trait.ITrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class TraitStrength implements ITrait {
    @Override
    public String getName() {
        return "Srength";
    }

    @Override
    public void update(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, Integer.MAX_VALUE, 2, false, false));
    }

    @Override
    public String getMessage() {
        return "trait.messages.strength";
    }
}
