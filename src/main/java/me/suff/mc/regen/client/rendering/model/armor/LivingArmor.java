package me.suff.mc.regen.client.rendering.model.armor;

import net.minecraft.world.entity.LivingEntity;

/* Created by Craig on 10/03/2021 */
public interface LivingArmor {
    LivingEntity getLiving();

    void setLiving(LivingEntity entity);
}
