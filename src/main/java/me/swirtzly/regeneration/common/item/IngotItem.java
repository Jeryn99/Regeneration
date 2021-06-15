package me.swirtzly.regeneration.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

/**
 * Created by Swirtzly
 * on 02/05/2020 @ 11:34
 */
public class IngotItem extends Item {

    public IngotItem() {
        super(new Item.Properties().rarity(Rarity.UNCOMMON).tab(ItemGroups.REGEN_TAB));
    }
}
