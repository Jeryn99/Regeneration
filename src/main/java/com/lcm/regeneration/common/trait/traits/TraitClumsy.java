package com.lcm.regeneration.common.trait.traits;

import com.lcm.regeneration.common.trait.ITrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TraitClumsy implements ITrait {

    @Override
    public void update(EntityPlayer player) {
        if (player.world.isRemote)
            return;

        NonNullList<ItemStack> inv = player.inventory.mainInventory;
        ItemStack stack = inv.get(player.world.rand.nextInt(inv.size()));
        ItemStack droppedStack = stack.copy();
        if (!stack.isEmpty() && player.ticksExisted % 2000 == 0) {
            player.dropItem(droppedStack, false);
            stack.shrink(1);
        }
    }
}
