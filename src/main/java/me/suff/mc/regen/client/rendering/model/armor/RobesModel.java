package me.suff.mc.regen.client.rendering.model.armor;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.suff.mc.regen.util.ClientUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class RobesModel extends HumanoidModel<LivingEntity> implements LivingArmor {
    private final ModelPart Body = null;
    private final ModelPart RightArm = null;
    private final ModelPart LeftArm = null;
    private final ModelPart RightLeg = null;
    private final ModelPart RightArmSteve = null;
    private final ModelPart LeftArmSteve = null;
    private final ModelPart LeftLeg = null;
    private final ModelPart Collar = null;
    private final ModelPart Cape = null;
    private ModelPart mainArmRight;
    private ModelPart mainArmLeft;
    private LivingEntity livingEntity = null;
    private EquipmentSlot slot = EquipmentSlot.HEAD;

    public RobesModel(ModelPart p_170677_, EquipmentSlot slot) {
        super(p_170677_);
        mainArmLeft = LeftArm;
        mainArmRight = RightArm;
        this.slot = slot;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if (slot == EquipmentSlot.HEAD) {
            matrixStack.pushPose();
            if (livingEntity.isShiftKeyDown()) {
                matrixStack.translate(0, 0.1, 0);
            }
            Collar.render(matrixStack, buffer, packedLight, packedOverlay);
            matrixStack.popPose();
        }
        if (slot == EquipmentSlot.CHEST) {
            updateArms(livingEntity);
            Body.render(matrixStack, buffer, packedLight, packedOverlay);
            mainArmRight.render(matrixStack, buffer, packedLight, packedOverlay);
            mainArmLeft.render(matrixStack, buffer, packedLight, packedOverlay);
            Cape.render(matrixStack, buffer, packedLight, packedOverlay);
        }
        if (slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
            RightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
            LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        }
    }

    public void updateArms(LivingEntity livingEntity) {
        if (livingEntity instanceof AbstractClientPlayer) {
            boolean isAlex = ClientUtil.isAlex(livingEntity);
            if (isAlex) {
                this.mainArmLeft = LeftArm;
                this.mainArmRight = RightArm;
            } else {
                this.mainArmLeft = LeftArmSteve;
                this.mainArmRight = RightArmSteve;
            }
        } else {
            this.mainArmLeft = LeftArmSteve;
            this.mainArmRight = RightArmSteve;
        }
      //TODO  leftArm = mainArmLeft;
     //   rightArm = mainArmRight;
    }

    public void renderCape(PoseStack matrixStackIn, VertexConsumer ivertexbuilder, int packedLightIn, int noOverlay) {
        this.Cape.render(matrixStackIn, ivertexbuilder, packedLightIn, noOverlay);
    }

    @Override
    public LivingEntity getLiving() {
        return livingEntity;
    }

    @Override
    public void setLiving(LivingEntity entity) {
        this.livingEntity = entity;
    }
}