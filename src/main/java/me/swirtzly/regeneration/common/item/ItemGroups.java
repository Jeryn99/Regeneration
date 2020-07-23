package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Created by Swirtzly
 * on 29/04/2020 @ 10:50
 */
public class ItemGroups {

    public static final MaterialGuard MATERIAL_GUARD = new MaterialGuard();

    public static final ItemGroup REGEN_TAB = new ItemGroup("regeneration") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegenObjects.Blocks.HAND_JAR.get());
        }
    };

    public static final ItemGroup REGEN_CLOTHING = new ItemGroup("regeneration") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegenObjects.Items.ROBES_CHEST.get());
        }
    };
}
