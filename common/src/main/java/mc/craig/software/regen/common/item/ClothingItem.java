package mc.craig.software.regen.common.item;

import mc.craig.software.regen.util.ClientUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

/* Created by Craig on 03/03/2021 */
public class ClothingItem extends ArmorItem implements ICustomArmorTexture {
    public ClothingItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        String gender = "";
        if (slot == EquipmentSlot.CHEST) {
            gender = (ClientUtil.isAlex(entity) ? "_alex" : "_steve");
        }
        return new ResourceLocation("regen:textures/entity/armour/" + Registry.ITEM.getKey(stack.getItem()).getPath() + gender + ".png");
    }

}
