package me.craig.software.regen.client.rendering.model.armor;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.craig.software.regen.util.ClientUtil;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class RobesModel extends BipedModel<LivingEntity> implements LivingArmor {
    private final ModelRenderer Body;
    private final ModelRenderer RightArm;
    private final ModelRenderer LeftArm;
    private final ModelRenderer RightLeg;
    private final ModelRenderer RightArmSteve;
    private final ModelRenderer LeftArmSteve;
    private final ModelRenderer LeftLeg;
    private final ModelRenderer Collar;
    private final ModelRenderer Cape;
    private ModelRenderer mainArmRight;
    private ModelRenderer mainArmLeft;
    private LivingEntity livingEntity = null;
    private EquipmentSlotType slot = EquipmentSlotType.HEAD;

    public RobesModel(EquipmentSlotType slotType) {
        super(1);
        this.slot = slotType;
        texWidth = 100;
        texHeight = 100;

        Body = new ModelRenderer(this);
        Body.setPos(0.0F, 0.0F, 0.0F);
        Body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        Body.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.25F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setPos(-5.0F, 2.0F, 0.0F);
        RightArm.texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);
        RightArm.texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.25F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(5.0F, 2.0F, 0.0F);
        LeftArm.texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);
        LeftArm.texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.25F, false);

        RightArmSteve = new ModelRenderer(this);
        RightArmSteve.setPos(-5.0F, 2.0F, 0.0F);
        RightArmSteve.texOffs(32, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        RightArmSteve.texOffs(48, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);

        LeftArmSteve = new ModelRenderer(this);
        LeftArmSteve.setPos(5.0F, 2.0F, 0.0F);
        LeftArmSteve.texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        LeftArmSteve.texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);


        RightLeg = new ModelRenderer(this);
        RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        RightLeg.texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);

        LeftLeg = new ModelRenderer(this);
        LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        LeftLeg.texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        LeftLeg.texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);

        Collar = new ModelRenderer(this);
        Collar.setPos(0.0F, 24.0F, 0.0F);
        Collar.texOffs(0, 84).addBox(-7.5F, -24.275F, -2.5F, 15.0F, 3.0F, 5.0F, 0.0F, false);
        Collar.texOffs(0, 64).addBox(-7.5F, -36.275F, -2.5F, 15.0F, 12.0F, 8.0F, 0.0F, false);

        Cape = new ModelRenderer(this);
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
        Body.addChild(Collar);

    }

    public static void capeBob(ModelRenderer p_239101_0_, float p_239101_2_) {
        //p_239101_0_.xRot += MathHelper.sin(p_239101_2_ * 0.03F) * 0.01F;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (slot == EquipmentSlotType.CHEST || slot == EquipmentSlotType.HEAD) {
            updateArms(livingEntity);
            Body.render(matrixStack, buffer, packedLight, packedOverlay);
            Body.visible = true;
            Collar.visible = true;

            if (slot == EquipmentSlotType.HEAD) return;
            mainArmRight.render(matrixStack, buffer, packedLight, packedOverlay);
            mainArmLeft.render(matrixStack, buffer, packedLight, packedOverlay);
            capeBob(this.Cape, livingEntity.tickCount);
            Cape.render(matrixStack, buffer, packedLight, packedOverlay);
        }
        if (slot == EquipmentSlotType.LEGS || slot == EquipmentSlotType.FEET) {
            RightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
            LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        }
    }

    public void updateArms(LivingEntity livingEntity) {
        if (livingEntity instanceof AbstractClientPlayerEntity) {
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

    public void renderCape(MatrixStack matrixStackIn, IVertexBuilder ivertexbuilder, int packedLightIn, int noOverlay) {
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