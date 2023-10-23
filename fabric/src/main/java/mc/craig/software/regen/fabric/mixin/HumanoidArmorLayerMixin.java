package mc.craig.software.regen.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mc.craig.software.regen.client.ArmorModelManager;
import mc.craig.software.regen.client.rendering.model.armor.LivingArmor;
import mc.craig.software.regen.common.item.ICustomArmorTexture;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Shadow
    protected abstract void setPartVisibility(A model, EquipmentSlot slot);

    @Shadow
    protected abstract boolean usesInnerModel(EquipmentSlot slot);

    @Shadow
    public abstract void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    @SuppressWarnings("unchecked")
    @Inject(at = @At("HEAD"), method = "renderArmorPiece", cancellable = true)
    private void renderArmorPiece(PoseStack poseStack, MultiBufferSource multiBufferSource, T livingEntity, EquipmentSlot equipmentSlot, int i, A humanoidModel, CallbackInfo callbackInfo) {
        ItemStack itemStack = livingEntity.getItemBySlot(equipmentSlot);
        if (itemStack.getItem() instanceof ICustomArmorTexture) {
            ArmorItem armorItem = (ArmorItem) itemStack.getItem();

            HumanoidModel<?> model = ArmorModelManager.getArmorModel(itemStack, livingEntity, equipmentSlot);

            if (model instanceof LivingArmor livingArmor) {
                livingArmor.setLiving(livingEntity);
            }

            if (model != null) {
                humanoidModel = (A) model;
            }

            if (armorItem.getType().getSlot() == equipmentSlot) {
                this.getParentModel().copyPropertiesTo(humanoidModel);
                this.setPartVisibility(humanoidModel, equipmentSlot);
                boolean bl = this.usesInnerModel(equipmentSlot);
                boolean bl2 = itemStack.hasFoil();
                if (armorItem instanceof DyeableArmorItem) {
                    int j = ((DyeableArmorItem) armorItem).getColor(itemStack);
                    float f = (float) (j >> 16 & 255) / 255.0F;
                    float g = (float) (j >> 8 & 255) / 255.0F;
                    float h = (float) (j & 255) / 255.0F;
                    this.renderModel(poseStack, multiBufferSource, i, livingEntity, itemStack, equipmentSlot, bl2, humanoidModel, bl, f, g, h, null);
                    this.renderModel(poseStack, multiBufferSource, i, livingEntity, itemStack, equipmentSlot, bl2, humanoidModel, bl, 1.0F, 1.0F, 1.0F, "overlay");
                } else {
                    this.renderModel(poseStack, multiBufferSource, i, livingEntity, itemStack, equipmentSlot, bl2, humanoidModel, bl, 1.0F, 1.0F, 1.0F, null);
                }

            }
            callbackInfo.cancel();
        }
    }

    private void renderModel(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, boolean bl, A humanoidModel, boolean bl2, float f, float g, float h, @Nullable String string) {
        ResourceLocation texture = ((ICustomArmorTexture) itemStack.getItem()).getArmorTexture(itemStack, livingEntity, equipmentSlot, string);
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(texture), false, bl);
        humanoidModel.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, f, g, h, 1.0F);
    }

}