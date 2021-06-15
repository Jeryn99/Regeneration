package me.suff.mc.regen.common.item;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.proxy.ClientProxy;
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
    public static String SWIFT_KEY = "yOulqmuNmMtget3CxeKbWwr0FVanx90M35TlMFdmHI0Rda3y3iMCqFp";

    public DyeableClothingItem(EquipmentSlotType slot) {
        super(ArmorMaterial.LEATHER, slot, new Item.Properties().rarity(Rarity.UNCOMMON).tab(ItemGroups.REGEN_TAB));
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);

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
        return (A) ClientProxy.getArmorModel(itemStack);
    }

    @Override
    public EquipmentSlotType getSlot() {
        return this.slot;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return stack.getOrCreateTag().contains(SWIFT_KEY) ? TIMELORD.toString() : "regeneration:textures/entity/armour/robes.png";
    }
}
