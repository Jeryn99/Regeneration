package me.suff.mc.regen.client.rendering.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.suff.mc.regen.util.ClientUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

/* Created by Craig on 03/03/2021 */
public class GuardModel extends HumanoidModel<LivingEntity> implements LivingArmor {
    private final ModelPart Head= null;
    private final ModelPart Body= null;
    private final ModelPart RightArm= null;
    private final ModelPart LeftArm= null;
    private final ModelPart RightArmSteve= null;
    private final ModelPart LeftArmSteve= null;
    private final ModelPart RightLeg= null;
    private final ModelPart LeftLeg = null;
    private ModelPart mainArmRight;
    private ModelPart mainArmLeft;
    private EquipmentSlot slot = EquipmentSlot.HEAD;
    private LivingEntity livingEntity;

    public GuardModel(EquipmentSlot slotType) {
        super(null);

    }


    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (slot == EquipmentSlot.HEAD) {
            Head.render(matrixStack, buffer, packedLight, packedOverlay);
        }
        if (slot == EquipmentSlot.CHEST) {
            updateArms(getLiving());
            Body.render(matrixStack, buffer, packedLight, packedOverlay);
            mainArmRight.render(matrixStack, buffer, packedLight, packedOverlay);
            mainArmLeft.render(matrixStack, buffer, packedLight, packedOverlay);
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
      //  leftArm = mainArmLeft;
     //   rightArm = mainArmRight;
        //TODO Revert or Figure out
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
