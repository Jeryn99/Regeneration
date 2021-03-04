package me.suff.mc.regen.common.item;

import me.suff.mc.regen.util.ClientUtil;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/* Created by Craig on 03/03/2021 */
public class ClothingItem extends ArmorItem {
    public ClothingItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "regen:textures/entity/armour/" + stack.getItem().getRegistryName().getPath() + ".png";
    }

    @Nullable
    @Override
    public < A extends BipedModel< ? > > A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        BipedModel< LivingEntity > model = (BipedModel< LivingEntity >) ClientUtil.getArmorModel(itemStack);
        return (A) model;
    }
}
