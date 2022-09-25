package me.craig.software.regen.client.rendering.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class RegenerationConeModel extends EntityModel<Entity> {
    private final ModelRenderer Head;
    private final ModelRenderer RightArm;
    private final ModelRenderer LeftArm;
    private final ModelRenderer _4_r1;

    public RegenerationConeModel() {
        texWidth = 80;
        texHeight = 80;

        Head = new ModelRenderer(this);
        Head.setPos(0.0F, 0.0F, 0.0F);
        Head.texOffs(22, 48).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 16.0F, 8.0F, 0.6F, false);
        Head.texOffs(22, 24).addBox(-4.0F, -33.2F, -4.0F, 8.0F, 16.0F, 8.0F, 0.8F, false);
        Head.texOffs(22, 0).addBox(-4.0F, -50.7F, -4.0F, 8.0F, 16.0F, 8.0F, 1.0F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setPos(-5.0F, 2.0F, 0.0F);
        RightArm.texOffs(0, 0).addBox(-3.0F, 9.0F, -2.0F, 4.0F, 16.0F, 4.0F, 0.4F, false);
        RightArm.texOffs(0, 20).addBox(-3.0F, 25.8F, -2.0F, 4.0F, 16.0F, 4.0F, 0.6F, false);
        RightArm.texOffs(0, 40).addBox(-3.0F, 43.05F, -2.0F, 4.0F, 16.0F, 4.0F, 0.8F, false);
        RightArm.texOffs(0, 60).addBox(-3.0F, 60.8F, -2.0F, 4.0F, 16.0F, 4.0F, 1.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(5.0F, 2.0F, 0.0F);


        _4_r1 = new ModelRenderer(this);
        _4_r1.setPos(1.0F, 25.8F, 0.0F);
        LeftArm.addChild(_4_r1);
        setRotationAngle(_4_r1, 0.0F, 3.1416F, 0.0F);
        _4_r1.texOffs(0, 60).addBox(-2.0F, 35.0F, -2.0F, 4.0F, 16.0F, 4.0F, 1.0F, false);
        _4_r1.texOffs(0, 40).addBox(-2.0F, 17.25F, -2.0F, 4.0F, 16.0F, 4.0F, 0.8F, false);
        _4_r1.texOffs(0, 20).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, 0.6F, false);
        _4_r1.texOffs(0, 0).addBox(-2.0F, -16.8F, -2.0F, 4.0F, 16.0F, 4.0F, 0.4F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public ModelRenderer getLeftArm() {
        return LeftArm;
    }

    public ModelRenderer getHead() {
        return Head;
    }

    public ModelRenderer getRightArm() {
        return RightArm;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

}