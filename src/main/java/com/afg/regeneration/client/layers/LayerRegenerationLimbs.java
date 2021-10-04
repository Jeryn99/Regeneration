package com.afg.regeneration.client.layers;

import com.afg.regeneration.capability.ITimelordCapability;
import com.afg.regeneration.capability.TimelordCapability;
import com.afg.regeneration.client.animation.LimbRotationUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

/**
 * Created by AFlyingGrayson on 8/8/17
 */
@SideOnly(Side.CLIENT)
public class LayerRegenerationLimbs implements LayerRenderer<EntityPlayer> {
    private static final ModelPlayer playerModelLargeArms = new ModelPlayer(0.55F, false);
    private static final ModelPlayer playerModelSmallArms = new ModelPlayer(0.55F, true);
    private final RenderPlayer playerRenderer;

    public LayerRegenerationLimbs(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;

    }

    public void doRenderLayer(EntityPlayer player, float p_177169_2_, float p_177169_3_, float p_177169_4_,
                              float p_177169_5_, float p_177169_6_, float p_177169_7_, float p_177169_8_) {

        if (player.hasCapability(TimelordCapability.TIMELORD_CAP, null) && player.getCapability(TimelordCapability.TIMELORD_CAP, null)
                .isTimelord()) {
            ITimelordCapability capability = player.getCapability(TimelordCapability.TIMELORD_CAP, null);

            if (capability.getRegenTicks() > 0) {

                Color color = new Color(255, 200, 0, 1);

                boolean smallArms = ((AbstractClientPlayer) player).getSkinType().equals("slim");
                ModelPlayer playerModel = smallArms ? playerModelSmallArms : playerModelLargeArms;

                GlStateManager.pushAttrib();
                GlStateManager.disableTexture2D();
                GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

                //Left Arm/Armwear
                LimbRotationUtil.createCustomModelRenderer(playerModel, 0, 0, -75, ModelBiped.class.getDeclaredFields()[4]);
                LimbRotationUtil.createCustomModelRenderer(playerModel, 0, 0, -75, ModelPlayer.class.getDeclaredFields()[0]);

                //Right Arm/Armwear
                LimbRotationUtil.createCustomModelRenderer(playerModel, 0, 0, 75, ModelBiped.class.getDeclaredFields()[3]);
                LimbRotationUtil.createCustomModelRenderer(playerModel, 0, 0, 75, ModelPlayer.class.getDeclaredFields()[1]);

                //Head/Headwear
                LimbRotationUtil.createCustomModelRenderer(playerModel, -20, 0, 0, ModelBiped.class.getDeclaredFields()[0]);
                LimbRotationUtil.createCustomModelRenderer(playerModel, -20, 0, 0, ModelBiped.class.getDeclaredFields()[1]);

                playerModel.setModelAttributes(this.playerRenderer.getMainModel());
                playerModel.render(player, p_177169_2_, p_177169_3_, p_177169_5_, p_177169_6_, p_177169_7_,
                        p_177169_8_);

                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.color(255, 255, 255, 255);
                GlStateManager.enableTexture2D();
                GlStateManager.popAttrib();

                if (capability.getRegenTicks() > 150) {
                    playerModelSmallArms.bipedBody.isHidden = playerModelSmallArms.bipedLeftLeg.isHidden = playerModelSmallArms.bipedRightLeg.isHidden = playerModelSmallArms.bipedBodyWear.isHidden = playerModelSmallArms.bipedHeadwear.isHidden = playerModelSmallArms.bipedLeftLegwear.isHidden = playerModelSmallArms.bipedRightLegwear.isHidden = false;

                    playerModelLargeArms.bipedBody.isHidden = playerModelLargeArms.bipedLeftLeg.isHidden = playerModelLargeArms.bipedRightLeg.isHidden = playerModelLargeArms.bipedBodyWear.isHidden = playerModelLargeArms.bipedHeadwear.isHidden = playerModelLargeArms.bipedLeftLegwear.isHidden = playerModelLargeArms.bipedRightLegwear.isHidden = false;
                } else {

                    playerModelSmallArms.bipedBody.isHidden = playerModelSmallArms.bipedLeftLeg.isHidden = playerModelSmallArms.bipedRightLeg.isHidden = playerModelSmallArms.bipedBodyWear.isHidden = playerModelSmallArms.bipedHeadwear.isHidden = playerModelSmallArms.bipedLeftLegwear.isHidden = playerModelSmallArms.bipedRightLegwear.isHidden = true;

                    playerModelLargeArms.bipedBody.isHidden = playerModelLargeArms.bipedLeftLeg.isHidden = playerModelLargeArms.bipedRightLeg.isHidden = playerModelLargeArms.bipedBodyWear.isHidden = playerModelLargeArms.bipedHeadwear.isHidden = playerModelLargeArms.bipedLeftLegwear.isHidden = playerModelLargeArms.bipedRightLegwear.isHidden = true;
                }

                if (capability.getRegenTicks() > 0) {
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder vertexbuffer = tessellator.getBuffer();

                    Random random = player.getRNG();

                    GlStateManager.enableCull();
                    GlStateManager.disableCull();
                    GlStateManager.shadeModel(7425);
                    GlStateManager.disableTexture2D();
                    GlStateManager.enableAlpha();
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.enableBlend();
                    GlStateManager.depthMask(false);

                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

                    float scale;

                    for (int i = 0; i < 8; i++) {
                        GlStateManager.pushMatrix();
                        playerModel.postRenderArm(0.0625F, EnumHandSide.RIGHT);
                        GlStateManager.translate(0f, -0.2f, 0f);

                        int green = 120 + random.nextInt(80);

                        GlStateManager.rotate(player.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
                        GlStateManager.scale(1.0f, 1.0f, 0.65f);
                        color = new Color(255, green, 0, 100);
                        scale = (float) capability.getRegenTicks() / 40;
                        float f3 = (float) capability.getRegenTicks() / 40;
                        vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                        vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
                                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        vertexbuffer.pos(0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
                                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        vertexbuffer.pos(0.0D, (double) scale, (double) (1.0F * f3)).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                                .endVertex();
                        vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
                                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        tessellator.draw();

                        GlStateManager.popMatrix();
                    }

                    for (int i = 0; i < 8; i++) {
                        GlStateManager.pushMatrix();
                        playerModel.postRenderArm(0.0625F, EnumHandSide.LEFT);
                        GlStateManager.translate(0f, -0.2f, 0f);

                        int green = 120 + random.nextInt(80);

                        GlStateManager.rotate(player.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
                        GlStateManager.scale(1.0f, 1.0f, 0.65f);
                        color = new Color(255, green, 0, 100);
                        scale = (float) capability.getRegenTicks() / 40;
                        float f3 = (float) capability.getRegenTicks() / 40;
                        vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                        vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
                                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        vertexbuffer.pos(0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
                                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        vertexbuffer.pos(0.0D, (double) scale, (double) (1.0F * f3)).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                                .endVertex();
                        vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
                                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        tessellator.draw();

                        GlStateManager.popMatrix();
                    }

                    for (int i = 0; i < 8; i++) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(0f, 0.3f, 0f);
                        GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);

                        int green = 120 + random.nextInt(80);

                        GlStateManager.rotate(player.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
                        GlStateManager.scale(1.0f, 1.0f, 0.65f);
                        color = new Color(255, green, 0, 100);
                        scale = (float) capability.getRegenTicks() / 40;
                        float f3 = (float) capability.getRegenTicks() / 40;
                        vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                        vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
                                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        vertexbuffer.pos(0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
                                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        vertexbuffer.pos(0.0D, (double) scale, (double) (1.0F * f3)).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                                .endVertex();
                        vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
                                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                        tessellator.draw();

                        GlStateManager.popMatrix();
                    }

                    GlStateManager.disableCull();
                    GlStateManager.shadeModel(7424);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.enableTexture2D();
                    GlStateManager.enableAlpha();
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.depthMask(true);
                }
            }
        }

    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
