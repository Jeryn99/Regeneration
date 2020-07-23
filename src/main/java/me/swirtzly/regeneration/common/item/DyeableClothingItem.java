package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.proxy.ClientProxy;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class DyeableClothingItem extends ArmorItem implements IDyeableArmorItem {

    public static ResourceLocation TIMELORD = new ResourceLocation(Regeneration.MODID, "textures/entity/armour/white_robes.png");


    public DyeableClothingItem(EquipmentSlotType slot) {
        super(ArmorMaterial.LEATHER, slot, new Item.Properties().rarity(Rarity.UNCOMMON).group(ItemGroups.REGEN_TAB));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);

        if (group == ItemGroups.REGEN_CLOTHING) {
            for (DyeColor value : DyeColor.values()) {
                ItemStack stack = new ItemStack(this);
                setColor(stack, value.getColorValue());
                items.add(stack);
            }
        }
    }

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) ClientProxy.getArmorModel(this);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return TIMELORD.toString();
    }
}
