package com.lcm.regeneration.common.trait.traits;

import com.lcm.regeneration.common.trait.ITrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TraitClumsy implements ITrait {

    private int timer;

    @Override
    public String getName() {
        return "Clumsy";
    }

    @Override
    public void update(EntityPlayer player) {
        if (player.world.isRemote)
            return;

        timer++;

        NonNullList<ItemStack> inv = player.inventory.mainInventory;
        ItemStack stack = inv.get(player.world.rand.nextInt(inv.size()));
        ItemStack droppedStack = stack.copy();
        if (!stack.isEmpty() && timer == 1573) {
            player.dropItem(droppedStack, false);
            stack.shrink(1);
            timer = 0;
        }
    }

    @Override
    public String getMessage() {
        return "trait.messages.clumsy";
    }
}
