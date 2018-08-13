package me.sub.regeneration.common.trait.traits;

import me.sub.regeneration.common.trait.ITrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

public class TraitHunger implements ITrait {
    @Override
    public String getName() {
        return "Hunger";
    }

    @Override
    public void update(EntityPlayer player) {
        FoodStats food = player.getFoodStats();
        if(player.ticksExisted % 130 == 0) {
            food.setFoodLevel(food.getFoodLevel() - 1);
        }
    }

    @Override
    public String getMessage() {
        return "trait.messages.hunger";
    }
}
