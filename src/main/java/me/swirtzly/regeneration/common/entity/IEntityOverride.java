package me.swirtzly.regeneration.common.entity;

import net.minecraft.item.ItemStack;

public interface IEntityOverride {

    void update(EntityItemOverride itemOverride);

    boolean shouldDie(ItemStack stack);
}
