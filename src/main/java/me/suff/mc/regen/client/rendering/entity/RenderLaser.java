package me.suff.mc.regen.client.rendering.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.suff.mc.regen.common.entities.LaserProjectile;
import me.suff.mc.regen.util.RenderHelp;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

/* Created by Craig on 01/03/2021 */
public class RenderLaser extends EntityRenderer<LaserProjectile> {

    public RenderLaser(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(LaserProjectile entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStack.pushPose();
        Vector3d vec1 = new Vector3d(entity.xOld, entity.yOld, entity.zOld);
        Vector3d vec2 = new Vector3d(entity.getX(), entity.getY(), entity.getZ());
        vec1 = vec2.subtract(vec1);
        vec2 = vec2.subtract(vec2);
        vec1 = vec1.normalize();
        double x_ = vec2.x - vec1.x;
        double y_ = vec2.y - vec1.y;
        double z_ = vec2.z - vec1.z;
        double diff = MathHelper.sqrt(x_ * x_ + z_ * z_);
        float yaw = (float) (Math.atan2(z_, x_) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(y_, diff) * 180.0D / 3.141592653589793D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-yaw));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(pitch));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        IVertexBuilder vertexBuilder = bufferIn.getBuffer(RenderType.lightning());
        RenderHelp.drawGlowingLine(matrixStack.last().pose(), vertexBuilder, 1F, 0.05F, 0, 0, 1, 1F, 15728640);
        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(LaserProjectile entity) {
        return null;
    }


}
