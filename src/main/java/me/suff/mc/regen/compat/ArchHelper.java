package me.suff.mc.regen.compat;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Created by Swirtzly
 * on 22/04/2020 @ 13:56
 */
public class ArchHelper {

    public static boolean storeRegenerations(ItemStack stack, int amount) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt("storedRegens", amount);
        return true;
    }

    public static boolean hasRegenerations(ItemStack stack) {
        return stack.getOrCreateTag().contains("storedRegens");
    }

    public static int getRegenerations(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        if (tag.contains("storedRegens")) {
            return tag.getInt("storedRegens");
        }
        return 0;
    }

}
