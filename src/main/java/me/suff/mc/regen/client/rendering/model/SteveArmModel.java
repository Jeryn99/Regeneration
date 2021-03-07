package me.suff.mc.regen.client.rendering.model;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class SteveArmModel extends EntityModel< Entity > {
    private final ModelRenderer RightArm;

    public SteveArmModel() {
        textureWidth = 64;
        textureHeight = 64;

        RightArm = new ModelRenderer(this);
        RightArm.setRotationPoint(-3.0F, 2.0F, -3.0F);
        RightArm.setTextureOffset(40, 16).addBox(0.5F, 10.0F, 0.5F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        RightArm.setTextureOffset(40, 32).addBox(0.5F, 10.0F, 0.5F, 4.0F, 12.0F, 4.0F, 0.25F, false);
    }

    @Override
    public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        RightArm.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}