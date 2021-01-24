package me.swirtzly.regeneration.client.rendering.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.common.entity.OverrideEntity;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Random;

public class ItemOverrideRenderer extends EntityRenderer< OverrideEntity > {

    private Vec3d primaryColor = new Vec3d(0.93F, 0.61F, 0.0F);
    private Vec3d secondaryColor = new Vec3d(1F, 0.5F, 0.18F);

    public ItemOverrideRenderer(EntityRendererManager rm) {
        super(rm);
    }

    static void makeGlowingBall(Minecraft mc, float f, Random rand, Vec3d primaryColor, Vec3d secondaryColor) {
        GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);

        for (int i = 0; i < 3; i++) {
            GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
            RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, primaryColor, 0);
            RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, secondaryColor, 0);
        }
        RenderUtil.finishRenderLightning();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(OverrideEntity entity) {
        return null;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    @Override
    public void doRender(OverrideEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.getItem().isEmpty()) return;
        Minecraft mc = Minecraft.getInstance();
        float f = 0.2f;
        Random rand = entity.world.rand;

        GlStateManager.pushMatrix();
        if (entity.getItem().getItem() == RegenObjects.Items.FOB_WATCH.get() && entity.getItem().getDamage() != RegenConfig.COMMON.regenCapacity.get()) {
            for (int j = 0; j < 2; j++) {
                RenderUtil.setupRenderLightning();
                GlStateManager.translated(x, y + 0.20, z);
                GlStateManager.scalef(0.7F, 0.7F, 0.7F);
                makeGlowingBall(mc, f, rand, primaryColor, secondaryColor);
            }
        }

        GlStateManager.translated(x, y + 0.17F, z);
        GlStateManager.rotatef(-entity.rotationYaw, 0, 1, 0);
        Minecraft.getInstance().getItemRenderer().renderItem(entity.getItem(), ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();
    }

}
