package craig.software.mc.regen.common.item.misc;

import craig.software.mc.regen.client.rendering.model.armor.LivingArmor;
import craig.software.mc.regen.util.ClientUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class ClothingItemRendering implements IClientItemExtensions {

    @Override
    public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        ClientUtil.clothingModels();
        HumanoidModel<LivingEntity> model = (HumanoidModel<LivingEntity>) ClientUtil.getArmorModel(itemStack, livingEntity);
        if (model instanceof LivingArmor) {
            ((LivingArmor) model).setLiving(livingEntity);
        }
        return model;
    }

    @Override
    public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        ClientUtil.clothingModels();
        HumanoidModel<LivingEntity> model = (HumanoidModel<LivingEntity>) ClientUtil.getArmorModel(itemStack, livingEntity);
        if (model instanceof LivingArmor) {
            ((LivingArmor) model).setLiving(livingEntity);
        }
        return model;
    }
}
