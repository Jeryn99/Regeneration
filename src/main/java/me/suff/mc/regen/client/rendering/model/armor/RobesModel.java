package me.suff.mc.regen.client.rendering.model.armor;// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class RobesModel extends BipedModel< LivingEntity > {
    private final ModelRenderer Body;
    private final ModelRenderer RightArm;
    private final ModelRenderer LeftArm;
    private final ModelRenderer RightLeg;
    private final ModelRenderer LeftLeg;
    private final ModelRenderer Collar;
    private final ModelRenderer Cape;

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

    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }

    public void setLivingEntity(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

   /*     if(livingEntity.limbSwing > 0) {
            if (livingEntity.isSprinting()) {
                Cape.rotateAngleX = (float) Math.toDegrees(35);
            } else {
                Cape.rotateAngleX = livingEntity.limbSwing;
            }
        }*/

        if (slot == EquipmentSlotType.HEAD) {
            matrixStack.pushPose();
            if (livingEntity.isShiftKeyDown()) {
                matrixStack.translate(0, 0.1, 0);
            }
            Collar.render(matrixStack, buffer, packedLight, packedOverlay);
            matrixStack.popPose();
        }
        if (slot == EquipmentSlotType.CHEST) {
            Body.render(matrixStack, buffer, packedLight, packedOverlay);
            RightArm.render(matrixStack, buffer, packedLight, packedOverlay);
            LeftArm.render(matrixStack, buffer, packedLight, packedOverlay);
            Cape.render(matrixStack, buffer, packedLight, packedOverlay);
        }
        if (slot == EquipmentSlotType.LEGS || slot == EquipmentSlotType.FEET) {
            RightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
            LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        }
    }

    public void renderCape(MatrixStack matrixStackIn, IVertexBuilder ivertexbuilder, int packedLightIn, int noOverlay) {
        this.Cape.render(matrixStackIn, ivertexbuilder, packedLightIn, noOverlay);
    }
}