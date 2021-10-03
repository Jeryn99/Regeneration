package me.suff.mc.regen.client.rendering.model;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ArmModel extends EntityModel<Entity> {
    private final ModelRenderer arm;

    public ArmModel(boolean alex) {
        texWidth = 64;
        texHeight = 64;

        if (alex) {
            arm = new ModelRenderer(this);
            arm.setPos(0.0F, 18.0F, -0.5F);
            arm.texOffs(40, 16).addBox(-1.5F, -6.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);
            arm.texOffs(40, 32).addBox(-1.5F, -6.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.25F, false);
        } else {
            arm = new ModelRenderer(this);
            arm.setPos(-3.0F, 2.0F, -3.0F);
            arm.texOffs(40, 16).addBox(0.5F, 10.0F, 0.5F, 4.0F, 12.0F, 4.0F, 0.0F, false);
            arm.texOffs(40, 32).addBox(0.5F, 10.0F, 0.5F, 4.0F, 12.0F, 4.0F, 0.25F, false);
        }
    }

    @Override
    public void setupAnim(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        arm.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}