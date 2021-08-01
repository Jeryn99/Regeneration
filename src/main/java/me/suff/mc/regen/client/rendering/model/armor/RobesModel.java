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
    private final ModelPart Body;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart RightLeg;
    private final ModelPart RightArmSteve;
    private final ModelPart LeftArmSteve;
    private final ModelPart LeftLeg;
    private final ModelPart Collar;
    private final ModelPart Cape;
    private ModelPart mainArmRight;
    private ModelPart mainArmLeft;
    private LivingEntity livingEntity = null;
    private EquipmentSlot slot = EquipmentSlot.HEAD;

    public RobesModel(EquipmentSlot slotType) {
        super(1);
        this.slot = slotType;
        texWidth = 100;
        texHeight = 100;

        Body = new ModelPart(this);
        Body.setPos(0.0F, 0.0F, 0.0F);
        Body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        Body.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.25F, false);

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

        Collar = new ModelPart(this);
        Collar.setPos(0.0F, 24.0F, 0.0F);
        Collar.texOffs(0, 84).addBox(-7.5F, -24.275F, -2.5F, 15.0F, 3.0F, 5.0F, 0.0F, false);
        Collar.texOffs(0, 64).addBox(-7.5F, -36.275F, -2.5F, 15.0F, 12.0F, 8.0F, 0.0F, false);

        Cape = new ModelPart(this);
        Cape.setPos(0.0F, 0.0F, 0.0F);
        Cape.texOffs(54, 16).addBox(-5.0F, -0.25F, 3.0F, 10.0F, 23.0F, 0.0F, 0.0F, false);

        head.visible = false;
        body = Body;
        leftArm = LeftArm;
        rightArm = RightArm;
        rightLeg = RightLeg;
        leftLeg = LeftLeg;

        mainArmLeft = LeftArm;
        mainArmRight = RightArm;
    }

    public static void capeBob(ModelPart p_239101_0_, float p_239101_2_) {
        //p_239101_0_.xRot += MathHelper.sin(p_239101_2_ * 0.03F) * 0.01F;
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
            capeBob(this.Cape, livingEntity.tickCount);
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
        leftArm = mainArmLeft;
        rightArm = mainArmRight;
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