package me.suff.mc.regen.common.entity;

import net.minecraft.item.ItemStack;

public interface IEntityOverride {

    void update(EntityItemOverride itemOverride);

    boolean shouldDie(ItemStack stack);
}
