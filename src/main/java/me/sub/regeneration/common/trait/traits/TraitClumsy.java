package me.sub.regeneration.common.trait.traits;

import me.sub.regeneration.common.trait.ITrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TraitClumsy implements ITrait {

    @Override
    public String getName() {
        return "Clumsy";
    }

    @Override
    public void update(EntityPlayer player) {
        if (player.world.isRemote)
            return;


        NonNullList<ItemStack> inv = player.inventory.mainInventory;
        ItemStack stack = inv.get(player.world.rand.nextInt(inv.size()));
        ItemStack droppedStack = stack.copy();
        if (!stack.isEmpty() && player.ticksExisted % 130 == 0) {
            player.dropItem(droppedStack, false);
            stack.shrink(1);
            }
        }

    @Override
    public String getMessage() {
        return "trait.messages.clumsy";
    }
}
