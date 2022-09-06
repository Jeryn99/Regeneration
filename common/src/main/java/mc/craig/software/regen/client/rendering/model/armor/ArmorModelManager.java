package mc.craig.software.regen.client.rendering.model.armor;

import mc.craig.software.regen.util.ClientUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ArmorModelManager implements ResourceManagerReloadListener {

    public static <T extends LivingEntity> HumanoidModel<?> getArmorModel(ItemStack itemStack, T livingEntity, EquipmentSlot equipmentSlot) {
        return ClientUtil.getArmorModel(itemStack, livingEntity);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        ClientUtil.clothingModels();
    }


}