package me.swirtzly.regeneration.client.rendering.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.Entity;

/**
 * Created by Swirtzly on 22/08/2019 @ 16:46
 */
public class HandModel extends EntityModel<Entity> {
    private final RendererModel arm;

    public HandModel(boolean isAlex) {
        texWidth = 64;
        texHeight = 64;

        if (isAlex) {
            arm = new RendererModel(this);
            arm.setPos(-2.0F, 12.0F, 0.0F);
            setRotationAngle(arm, 0.0F, 0.0F, 3.1416F);
            arm.cubes.add(new ModelBox(arm, 40, 16, -3.0F, -11.5F, 0.0F, 3, 12, 4, 0.0F, false));
            arm.cubes.add(new ModelBox(arm, 40, 32, -3.0F, -11.5F, 0.0F, 3, 12, 4, 0.375F, false));
        } else {
            arm = new RendererModel(this);
            arm.setPos(-2.0F, 12.0F, 0.0F);
            setRotationAngle(arm, 0.0F, 0.0F, 3.1416F);
            arm.cubes.add(new ModelBox(arm, 40, 16, -3.0F, -11.5F, -2.0F, 3, 12, 4, 0.0F, false));
            arm.cubes.add(new ModelBox(arm, 40, 32, -3.0F, -11.5F, -2.0F, 3, 12, 4, 0.375F, false));
        }
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        arm.render(f5);
    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
