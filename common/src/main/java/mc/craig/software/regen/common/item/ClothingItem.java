package mc.craig.software.regen.common.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

/* Created by Craig on 03/03/2021 */
public class ClothingItem extends ArmorItem {
    public ClothingItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    //TODO
/*    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        String gender = "";
        if (slot == EquipmentSlot.CHEST) {
            gender = (ClientUtil.isAlex(entity) ? "_alex" : "_steve");
        }
        return "regen:textures/entity/armour/" + Registry.ITEM.getKey(stack.getItem()).getPath() + gender + ".png";
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new ClothingItemRendering());
    }*/
}
