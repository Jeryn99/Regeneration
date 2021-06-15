package me.suff.mc.regen.client.rendering.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import me.suff.mc.regen.common.entity.LaserEntity;
import me.suff.mc.regen.util.client.RenderUtil;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class LaserRenderer extends EntityRenderer<LaserEntity> {

    public LaserRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void postRender(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
    }

    @Override
    public void render(LaserEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        RenderUtil.setupRenderLightning();
        Vec3d vec1 = new Vec3d(entity.xOld, entity.yOld, entity.zOld);
        Vec3d vec2 = new Vec3d(entity.x, entity.y, entity.z);
        vec1 = vec2.subtract(vec1);
        vec2 = vec2.subtract(vec2);
        vec1 = vec1.normalize();
        double x_ = vec2.x - vec1.x;
        double y_ = vec2.y - vec1.y;
        double z_ = vec2.z - vec1.z;
        double diff = (double) MathHelper.sqrt(x_ * x_ + z_ * z_);
        float yaw = (float) (Math.atan2(z_, x_) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) (-(Math.atan2(y_, diff) * 180.0D / 3.141592653589793D));
        GlStateManager.rotatef(-yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(pitch, 1.0F, 0.0F, 0.0F);
        RenderUtil.drawGlowingLine(Vec3d.ZERO, new Vec3d(0.0D, 0.0D, 1.0D), entity.scale, entity.getColor(), 1.0F);
        RenderUtil.finishRenderLightning();
        GlStateManager.popMatrix();
    }

    @Nullable
    protected ResourceLocation getTextureLocation(LaserEntity entity) {
        return null;
    }
}
