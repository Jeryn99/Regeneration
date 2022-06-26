package craig.software.mc.regen.common.item.misc;

import craig.software.mc.regen.client.rendering.model.armor.LivingArmor;
import craig.software.mc.regen.util.ClientUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClothingItemRendering implements IItemRenderProperties {

    @NotNull
    @Override
    public @Nullable HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
        ClientUtil.clothingModels();
        HumanoidModel<LivingEntity> model = (HumanoidModel<LivingEntity>) ClientUtil.getArmorModel(itemStack, entityLiving);
        if (model instanceof LivingArmor) {
            ((LivingArmor) model).setLiving(entityLiving);
        }
        return model;
    }
}
