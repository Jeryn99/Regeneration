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

public class ClothingItem extends ArmorItem {


    public static ResourceLocation TIMELORD = new ResourceLocation(Regeneration.MODID, "textures/entity/armour/gaurd_armour.png");
    private String prefix = null;

    public ClothingItem(EquipmentSlotType slot) {
        super(ArmorMaterial.GOLD, slot, new Item.Properties().rarity(Rarity.UNCOMMON).group(ItemGroups.REGEN_TAB));
        this.prefix = "guard_SLOT";
    }

    public ClothingItem(EquipmentSlotType slot, String prefix) {
        super(ArmorMaterial.GOLD, slot, new Item.Properties().rarity(Rarity.UNCOMMON).group(ItemGroups.REGEN_TAB));
        this.prefix = prefix;
    }

    @Override
    public EquipmentSlotType getEquipmentSlot() {
        return this.slot;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList< ItemStack > items) {
        super.fillItemGroup(group, items);
    }

    @Nullable
    @Override
    public < A extends BipedModel< ? > > A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) ClientProxy.getArmorModel(itemStack);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        String path = "regeneration:textures/entity/armour/" + prefix + ".png";
        path = path.replaceAll("SLOT", slot.getName());
        String result = stack.getOrCreateTag().contains(DyeableClothingItem.SWIFT_KEY) ? TIMELORD.toString() : path;
        return result;
    }
}
