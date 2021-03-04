package me.swirtzly.regen.common.item;

import me.swirtzly.regen.common.objects.RItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

/* Created by Craig on 03/03/2021 */
public class RMaterials {

    private static final int[] DMG_TIME = new int[]{3, 6, 8, 3};

    public static IArmorMaterial TIMELORD = new IArmorMaterial() {
        @Override
        public int getDurability(EquipmentSlotType slotIn) {
            return 35;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotIn) {
            return DMG_TIME[slotIn.getIndex()];
        }

        @Override
        public int getEnchantability() {
            return 15;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(RItems.ZINC.get());
        }

        @Override
        public String getName() {
            return "timelord";
        }

        @Override
        public float getToughness() {
            return 2.5F;
        }

        @Override
        public float getKnockbackResistance() {
            return 1;
        }
    };

}
