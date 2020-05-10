package me.swirtzly.regeneration.client.rendering.model;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.common.entity.CrackEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Swirtzly
 * on 10/05/2020 @ 22:41
 */
public class CrackModel extends EntityModel<CrackEntity> {

    private final RendererModel bb_main;

    public CrackModel() {
        textureWidth = 128;
        textureHeight = 128;

        bb_main = new RendererModel(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -24.0F, -32.0F, 7.5F, 48, 32, 0, 0.0F, false));
    }

    @Override
    public void render(CrackEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        GlStateManager.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(Regeneration.MODID, "textures/entity/crack.png"));
        bb_main.render(f5);
        GlStateManager.popMatrix();
    }

}
