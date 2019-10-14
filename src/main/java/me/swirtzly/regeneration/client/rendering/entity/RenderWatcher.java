package me.swirtzly.regeneration.client.rendering.entity;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.entity.EntityWatcher;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by Swirtzly
 * on 22/09/2019 @ 20:59
 */
public class RenderWatcher extends RenderLiving<EntityWatcher> {

    private ResourceLocation texture = new ResourceLocation(RegenerationMod.MODID, "textures/entity/the_watcher.png");

    public RenderWatcher(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelPlayer(0.1F, false), 0);
    }

    @Override
    protected void renderModel(EntityWatcher entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.8F);
        super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityWatcher entity) {
        return texture;
    }
}
