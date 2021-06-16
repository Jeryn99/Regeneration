package me.suff.mc.regen.common.item;

import me.suff.mc.regen.handlers.RegenObjects;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Created by Craig
 * on 29/04/2020 @ 10:50
 */
public class ItemGroups {

    public static final ItemGroup REGEN_TAB = new ItemGroup("regeneration") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegenObjects.Blocks.HAND_JAR.get());
        }
    };

    public static final ItemGroup REGEN_CLOTHING = new ItemGroup("regeneration_clothes") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegenObjects.Items.ROBES_CHEST.get());
        }
    };
}
