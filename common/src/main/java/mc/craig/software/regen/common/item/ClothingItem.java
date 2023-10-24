package mc.craig.software.regen.common.item;

import mc.craig.software.regen.util.ClientUtil;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

/* Created by Craig on 03/03/2021 */
public class ClothingItem extends ArmorItem implements ICustomArmorTexture {
    private final String texture;

    public ClothingItem(String texture, ArmorMaterial materialIn, ArmorItem.Type type, Properties builderIn) {
        super(materialIn, type, builderIn);
        this.texture = texture;
    }


    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        String skinType = (ClientUtil.isAlex(entity) ? "alex_" : "steve_");
        return new ResourceLocation(RConstants.MODID, "textures/entity/armour/" + skinType + texture + ".png");
    }

}
