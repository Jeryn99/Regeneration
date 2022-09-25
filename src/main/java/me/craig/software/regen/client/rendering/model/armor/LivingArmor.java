package me.craig.software.regen.client.rendering.model.armor;

import net.minecraft.entity.LivingEntity;

/* Created by Craig on 10/03/2021 */
public interface LivingArmor {
    LivingEntity getLiving();

    void setLiving(LivingEntity entity);
}
