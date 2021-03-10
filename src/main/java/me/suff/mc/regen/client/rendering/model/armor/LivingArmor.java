package me.suff.mc.regen.client.rendering.model.armor;

import net.minecraft.entity.LivingEntity;

/* Created by Craig on 10/03/2021 */
public interface LivingArmor {
    void setLiving(LivingEntity entity);

    LivingEntity getLiving();
}
