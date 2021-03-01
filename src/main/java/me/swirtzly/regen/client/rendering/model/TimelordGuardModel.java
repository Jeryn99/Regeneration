package me.swirtzly.regen.client.rendering.model;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.regen.common.entities.TimelordEntity;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;

import static me.swirtzly.regen.common.regen.state.RegenStates.REGENERATING;

public class TimelordGuardModel extends PlayerModel< TimelordEntity > {
    private final ModelRenderer Head;
    private final ModelRenderer Body;
    private final ModelRenderer RightArm;
    private final ModelRenderer LeftArm;
    private final ModelRenderer RightLeg;
    private final ModelRenderer LeftLeg;

    public TimelordGuardModel() {
        super(1, false);
        textureWidth = 80;
        textureHeight = 80;

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        Head.setTextureOffset(48, 64).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);
        Head.setTextureOffset(0, 64).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.6F, false);

        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.setTextureOffset(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        Body.setTextureOffset(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.25F, false);
        Body.setTextureOffset(32, 69).addBox(-4.0F, 10.0F, -2.0F, 8.0F, 6.0F, 4.0F, 0.25F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        RightArm.setTextureOffset(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);
        RightArm.setTextureOffset(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.25F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        LeftArm.setTextureOffset(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);
        LeftArm.setTextureOffset(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.25F, false);

        RightLeg = new ModelRenderer(this);
        RightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        RightLeg.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        RightLeg.setTextureOffset(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);

        LeftLeg = new ModelRenderer(this);
        LeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        LeftLeg.setTextureOffset(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        LeftLeg.setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);
    }

    @Override
    public void setRotationAngles(TimelordEntity timelordEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        IRegen regenData = RegenCap.get(timelordEntity).orElseGet(null);
/*

        if (timelordEntity.getHeldItemMainhand().getItem() instanceof GunItem && regenData.getState() != PlayerUtil.RegenState.REGENERATING) {
            rightArmPose = ArmPose.BOW_AND_ARROW;
        }
*/

        if (regenData.getCurrentState() == REGENERATING) {
            rightArmPose = ArmPose.EMPTY;
        }

        super.setRotationAngles(timelordEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        Head.copyModelAngles(bipedHead);
        Body.copyModelAngles(bipedBody);
        LeftArm.copyModelAngles(bipedLeftArm);
        RightArm.copyModelAngles(bipedRightArm);
        RightLeg.copyModelAngles(bipedRightLeg);
        LeftLeg.copyModelAngles(bipedLeftLeg);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        //head.render(matrixStack, buffer, packedLight, packedOverlay);
        Head.render(matrixStack, buffer, packedLight, packedOverlay);
        Body.render(matrixStack, buffer, packedLight, packedOverlay);
        RightArm.render(matrixStack, buffer, packedLight, packedOverlay);
        LeftArm.render(matrixStack, buffer, packedLight, packedOverlay);
        RightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}