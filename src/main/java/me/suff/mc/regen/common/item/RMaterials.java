package me.suff.mc.regen.common.item;

import me.suff.mc.regen.common.objects.RItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

/* Created by Craig on 03/03/2021 */
public class RMaterials {

    private static final int[] DMG_TIME = new int[]{3, 6, 8, 3};

    public static IArmorMaterial TIMELORD = new IArmorMaterial() {
        @Override
        public int getDurabilityForSlot(EquipmentSlotType slotIn) {
            switch (slotIn) {
                case HEAD:
                    return 390;
                case CHEST:
                    return 550;
                case LEGS:
                    return 510;
                case FEET:
                    return 450;
                case MAINHAND:
                    break;
                case OFFHAND:
                    break;
                default:
                    return 300;
            }
            return 300;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlotType slotIn) {
            return DMG_TIME[slotIn.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 20;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_DIAMOND;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(RItems.ZINC.get());
        }

        //ClientSide only variable
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
            return 1F;
        }
    };

}
