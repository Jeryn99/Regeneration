package me.suff.mc.regen.client.rendering.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.suff.mc.regen.util.ClientUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;

/* Created by Craig on 03/03/2021 */
public class GuardModel extends HumanoidModel<LivingEntity> implements LivingArmor {
    private final ModelPart Head;
    private final ModelPart Body;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart RightArmSteve;
    private final ModelPart LeftArmSteve;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private ModelPart mainArmRight;
    private ModelPart mainArmLeft;
    private EquipmentSlot slot = EquipmentSlot.HEAD;
    private LivingEntity livingEntity;

    public GuardModel(EquipmentSlot slotType) {
        super(1);
        this.slot = slotType;
        texWidth = 80;
        texHeight = 80;

        Head = new ModelPart(this);
        Head.setPos(0.0F, 0.0F, 0.0F);
        Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        Head.texOffs(48, 64).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);
        Head.texOffs(0, 64).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.6F, false);

        Body = new ModelPart(this);
        Body.setPos(0.0F, 0.0F, 0.0F);
        Body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        Body.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.25F, false);
        Body.texOffs(32, 69).addBox(-4.0F, 10.0F, -2.0F, 8.0F, 6.0F, 4.0F, 0.25F, false);

        RightArm = new ModelPart(this);
        RightArm.setPos(-5.0F, 2.0F, 0.0F);
        RightArm.texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);
        RightArm.texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.25F, false);

        LeftArm = new ModelPart(this);
        LeftArm.setPos(5.0F, 2.0F, 0.0F);
        LeftArm.texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);
        LeftArm.texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.25F, false);

        RightArmSteve = new ModelPart(this);
        RightArmSteve.setPos(-5.0F, 2.0F, 0.0F);
        RightArmSteve.texOffs(32, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        RightArmSteve.texOffs(48, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);

        LeftArmSteve = new ModelPart(this);
        LeftArmSteve.setPos(5.0F, 2.0F, 0.0F);
        LeftArmSteve.texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        LeftArmSteve.texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);


        RightLeg = new ModelPart(this);
        RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        RightLeg.texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);

        LeftLeg = new ModelPart(this);
        LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        LeftLeg.texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        LeftLeg.texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);

        hat.visible = false;
        head = Head;
        body = Body;
        leftArm = LeftArm;
        rightArm = RightArm;
        rightLeg = RightLeg;
        leftLeg = LeftLeg;
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
        leftArm = mainArmLeft;
        rightArm = mainArmRight;
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
