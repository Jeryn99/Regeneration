package me.suff.mc.regen.client.rendering.model;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ContainerModel extends EntityModel<Entity> {

    public static ResourceLocation CONTAINER_TEXTURE = new ResourceLocation(RConstants.MODID, "textures/tile/container.png");

    public final ModelRenderer Lid;
    public final ModelRenderer Jar;

    public ContainerModel() {
        texWidth = 64;
        texHeight = 64;

        Lid = new ModelRenderer(this);
        Lid.setPos(0.0F, 12.0F, 4.0F);
        Lid.texOffs(0, 0).addBox(-4.0F, -2.0F, -8.0F, 8.0F, 2.0F, 8.0F, 0.2F, false);
        Lid.texOffs(0, 22).addBox(-4.0F, -2.0F, -8.0F, 8.0F, 2.0F, 8.0F, 0.0F, false);

        Jar = new ModelRenderer(this);
        Jar.setPos(0.0F, 24.0F, 0.0F);
        Jar.texOffs(0, 22).addBox(-1.0F, -13.0F, 4.0F, 2.0F, 5.0F, 1.0F, 0.0F, false);
        Jar.texOffs(17, 43).addBox(-3.0F, -12.0F, 3.0F, 6.0F, 10.0F, 1.0F, 0.0F, false);
        Jar.texOffs(35, 47).addBox(-2.5F, -11.75F, -3.5F, 5.0F, 10.0F, 7.0F, -0.2F, false);
        Jar.texOffs(26, 26).addBox(-2.5F, -13.0F, -3.0F, 5.0F, 11.0F, 6.0F, 0.0F, false);
        Jar.texOffs(32, 0).addBox(-3.0F, -12.0F, -4.0F, 6.0F, 10.0F, 1.0F, 0.0F, false);
        Jar.texOffs(0, 0).addBox(-1.0F, -10.0F, -5.0F, 2.0F, 7.0F, 1.0F, 0.0F, false);
        Jar.texOffs(24, 14).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 2.0F, 8.0F, 0.0F, false);
        Jar.texOffs(0, 12).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 2.0F, 8.0F, 0.2F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Lid.render(matrixStack, buffer, packedLight, packedOverlay);
        Jar.render(matrixStack, buffer, packedLight, packedOverlay);
    }

}