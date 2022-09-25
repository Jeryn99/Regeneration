package mc.craig.software.regen.forge.mixin;

import mc.craig.software.regen.client.ArmorModelManager;
import mc.craig.software.regen.client.rendering.model.armor.LivingArmor;
import mc.craig.software.regen.common.item.ICustomArmorTexture;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Shadow
    protected abstract void setPartVisibility(A model, EquipmentSlot slot);

    @Inject(at = @At("HEAD"), method = "getArmorResource", cancellable = true, remap = false)
    private void getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type, CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (stack.getItem() instanceof ICustomArmorTexture customArmorTexture) {
            callbackInfoReturnable.setReturnValue(customArmorTexture.getArmorTexture(stack, entity, slot, type));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(at = @At("HEAD"), method = "getArmorModelHook", cancellable = true, remap = false)
    private void getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> model, CallbackInfoReturnable<Model> ci) {
        HumanoidModel m = ArmorModelManager.getArmorModel(itemStack, entity, slot);

        if(m == null) return;

        if (m instanceof LivingArmor livingArmor) {
            livingArmor.setLiving(entity);
        }

        if (model != null) {
            model.copyPropertiesTo(m);
            this.setPartVisibility((A) m, slot);
            ci.setReturnValue(m);
        }
    }
}