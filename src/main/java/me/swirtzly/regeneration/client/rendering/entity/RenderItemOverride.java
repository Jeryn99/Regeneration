package me.swirtzly.regeneration.client.rendering.entity;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.common.entity.EntityItemOverride;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Random;

public class RenderItemOverride extends Render<EntityItemOverride> {

    private Vec3d primaryColor = new Vec3d(0.93F, 0.61F, 0.0F);
    private Vec3d secondaryColor = new Vec3d(1F, 0.5F, 0.18F);

    public RenderItemOverride(RenderManager rm) {
        super(rm);
    }

    static void makeGlowingBall(Minecraft mc, float f, Random rand, Vec3d primaryColor, Vec3d secondaryColor) {
        GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);

        for (int i = 0; i < 3; i++) {
            GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
            RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, primaryColor, 0);
            RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, secondaryColor, 0);
        }
        RenderUtil.finishRenderLightning();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityItemOverride entity) {
        return null;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    @Override
    public void doRender(EntityItemOverride entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.getItem().isEmpty())
            return;
        Minecraft mc = Minecraft.getMinecraft();
        float f = 0.2f;
        Random rand = entity.world.rand;

        GlStateManager.pushMatrix();
        if (entity.getItem().getItem() == RegenObjects.Items.FOB_WATCH && entity.getItem().getItemDamage() != RegenConfig.regenCapacity) {
            for (int j = 0; j < 2; j++) {
                RenderUtil.setupRenderLightning();
                GlStateManager.translate(x, y + 0.20, z);
                GlStateManager.scale(0.7F, 0.7F, 0.7F);
                makeGlowingBall(mc, f, rand, primaryColor, secondaryColor);
            }
        }

        GlStateManager.pushMatrix();
        if (mc.objectMouseOver != null) {
            Entity look = mc.objectMouseOver.entityHit;
            if (look != null && look == entity) {
                float offset = MathHelper.cos(entity.ticksExisted * 0.1F) * -0.09F;
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0.4F, 0);
                GlStateManager.scale(0.60F, 0.60F, 0.60F);
                this.renderLivingLabel(entity, new TextComponentTranslation("right.click", Minecraft.getMinecraft().gameSettings.keyBindUseItem.getDisplayName()).getUnformattedComponentText(), x, y + 0.4 + offset, z, 46);
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.popMatrix();

        GlStateManager.translate(x, y + 0.17F, z);
        GlStateManager.rotate(-entity.rotationYaw, 0, 1, 0);
        Minecraft.getMinecraft().getRenderItem().renderItem(entity.getItem(), ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();
    }


}