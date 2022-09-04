package mc.craig.software.regen.common.item;

import mc.craig.software.regen.common.item.misc.ClothingItemRendering;
import mc.craig.software.regen.util.ClientUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;

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
        return "regen:textures/entity/armour/" + ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath() + gender + ".png";
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new ClothingItemRendering());
    }
}
