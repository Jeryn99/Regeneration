package me.swirtzly.regeneration.client.rendering.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.util.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Random;

public class LindosRenderer extends EntityRenderer<Entity> {

    private Vec3d primaryColor = new Vec3d(0.93F, 0.61F, 0.0F);
    private Vec3d secondaryColor = new Vec3d(1F, 0.5F, 0.18F);

    public LindosRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Random rand = entity.world.rand;
        float f = 0.1F;
        for (int j = 0; j < 2; j++) {
            RenderUtil.setupRenderLightning();
            GlStateManager.translated(x, y + 0.20, z);
            GlStateManager.scalef(0.9F, 0.9F, 0.9F);
            ItemOverrideRenderer.makeGlowingBall(mc, f, rand, primaryColor, secondaryColor);
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
