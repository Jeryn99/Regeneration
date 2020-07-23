package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class MaterialGuard implements IArmorMaterial {


    private final int[] MAX_DAMAGE_ARRAY = new int[]{3, 6, 8, 3};

    @Override
    public int getDurability(EquipmentSlotType slotIn) {
        return 0;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * 44;
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.fromItems(RegenObjects.Items.GAL_INGOT.get());
    }

    @Override
    public String getName() {
        return "gallifreyan_metal";
    }

    @Override
    public float getToughness() {
        return 3.5F;
    }
}
