package me.suff.mc.regen.common.item;

import me.suff.mc.regen.common.objects.RItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

/* Created by Craig on 03/03/2021 */
public class RMaterials {

    private static final int[] DMG_TIME = new int[]{3, 6, 8, 3};

    public static ArmorMaterial TIMELORD = new ArmorMaterial() {
        @Override
        public int getDurabilityForSlot(EquipmentSlot slotIn) {
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
                case OFFHAND:
                    break;
                default:
                    return 300;
            }
            return 300;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slotIn) {
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
