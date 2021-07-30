package me.suff.mc.regen.common.item;

import me.suff.mc.regen.client.rendering.model.armor.LivingArmor;
import me.suff.mc.regen.util.ClientUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.Item.Properties;

/* Created by Craig on 03/03/2021 */
public class ClothingItem extends ArmorItem {
    public ClothingItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        String gender = "";
        if (slot == EquipmentSlot.CHEST) {
            gender = (ClientUtil.isAlex(entity) ? "_alex" : "_steve");
        }
        return "regen:textures/entity/armour/" + stack.getItem().getRegistryName().getPath() + gender + ".png";
    }

    @Nullable
    @Override
    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
        HumanoidModel<LivingEntity> model = (HumanoidModel<LivingEntity>) ClientUtil.getArmorModel(itemStack);
        if (model instanceof LivingArmor) {
            ((LivingArmor) model).setLiving(entityLiving);
        }
        return (A) model;
    }
}
