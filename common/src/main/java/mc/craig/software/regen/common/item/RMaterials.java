package mc.craig.software.regen.common.item;

import mc.craig.software.regen.common.objects.RItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

/* Created by Craig on 03/03/2021 */
public class RMaterials {

    private static final int[] DMG_TIME = new int[]{3, 6, 8, 3};

    public static ArmorMaterial TIMELORD = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            switch (type) {
                case HELMET:
                    return 390;
                case CHESTPLATE:
                    return 550;
                case LEGGINGS:
                    return 510;
                case BOOTS:
                    return 450;
                default:
                    return 300;
            }
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return DMG_TIME[type.getSlot().getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 20;
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_DIAMOND;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(RItems.ZINC.get());
        }

        //ClientSide only variable
        @Override
        public @NotNull String getName() {
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
