package me.sub.common.post;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.awt.*;

/**
 * Created by Sub
 * on 24/09/2018.
 */
public class PotionPost extends Potion {

    protected PotionPost() {
        super(true, Color.RED.hashCode());
    }

    @Override
    public void performEffect(EntityLivingBase living, int amplifier) {
        if (living.ticksExisted % 100 == 0) {
            living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE));
        }
    }


}
