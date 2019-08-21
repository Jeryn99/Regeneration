package me.swirtzly.regeneration.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHand extends ModelBase {

    private final ModelRenderer arm;

    public ModelHand(boolean isAlex) {
        textureWidth = 64;
        textureHeight = 64;

        if (isAlex) {
            arm = new ModelRenderer(this);
            arm.setRotationPoint(-2.0F, 12.0F, 0.0F);
            setRotationAngle(arm, 0.0F, 0.0F, 3.1416F);
            arm.cubeList.add(new ModelBox(arm, 40, 16, -3.0F, -11.5F, 0.0F, 3, 12, 4, 0.0F, false));
            arm.cubeList.add(new ModelBox(arm, 40, 32, -3.0F, -11.5F, 0.0F, 3, 12, 4, 0.375F, false));
        } else {
            arm = new ModelRenderer(this);
            arm.setRotationPoint(-2.0F, 12.0F, 0.0F);
            setRotationAngle(arm, 0.0F, 0.0F, 3.1416F);
            arm.cubeList.add(new ModelBox(arm, 40, 16, -3.0F, -11.5F, -2.0F, 3, 12, 4, 0.0F, false));
            arm.cubeList.add(new ModelBox(arm, 40, 32, -3.0F, -11.5F, -2.0F, 3, 12, 4, 0.375F, false));
        }
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        arm.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
