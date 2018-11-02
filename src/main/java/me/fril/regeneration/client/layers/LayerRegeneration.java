package me.fril.regeneration.client.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Random;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.states.RegenTypes;
import me.fril.regeneration.util.RenderUtil;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class LayerRegeneration implements LayerRenderer<EntityPlayer> {

    public static final ModelPlayer playerModelSteve = new ModelPlayer(0.1F, false);
    public static final ModelPlayer playerModelAlex = new ModelPlayer(0.1F, true);
    private RenderPlayer playerRenderer;

    public LayerRegeneration(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }

    @Override
    public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        IRegeneration handler = CapabilityRegeneration.get(player);

        if (handler != null) {
            //Render the Fiery Regeneration
            if (handler.isRegenerating() && handler.getSolaceTicks() >= 200 && !handler.isInGracePeriod()) {
                if (handler.getType() == RegenTypes.FIERY) {
                    renderFieryRegen(playerRenderer, handler, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }

            //Glowing Hands
            renderGlowingHands(player, handler, scale);
        }
    }


    private void renderGlowingHands(EntityPlayer player, IRegeneration handler, float scale) {

        if (handler != null && handler.isGlowing()) {
            Color primaryColor = handler.getPrimaryColor();
            Color secondaryColor = handler.getSecondaryColor();

            Minecraft mc = Minecraft.getMinecraft();
            Random rand = player.world.rand;
            float f = 0.2F;

            for (int j = 0; j < 2; j++) {
                RenderUtil.setupRenderLightning();

                if (j == 0)
                    playerRenderer.getMainModel().bipedRightArm.postRender(scale);
                else
                    playerRenderer.getMainModel().bipedLeftArm.postRender(scale);

                GlStateManager.scale(1.5F, 1.5F, 1.5F);

                if (player.isSneaking()) {
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
                }

                GlStateManager.translate(0, 0.3F, 0);
                GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);

                for (int i = 0; i < 15; i++) {
                    GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
                    RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, primaryColor, 0);
                    RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, secondaryColor, 0);
                }

                RenderUtil.finishRenderLightning();
            }
        }
    }


    private void renderFieryRegen(RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float v, float v1, float v2, float v3, float v4, float v5, float v6) {
        ModelBiped model = (ModelBiped) renderLivingBase.getMainModel();

        // State manager changes
        GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(false);
        RenderUtil.setLightmapTextureCoords(65, 65);

        NBTTagCompound style = capability.getStyle();
        Color primaryColor = new Color(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
        Color secondaryColor = new Color(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));

        float primaryScale = capability.getTicksRegenerating() * 40f;
        float secondaryScale = capability.getTicksRegenerating() * 70f;

        // Render right cone
        GlStateManager.pushMatrix();
        model.postRenderArm(0.0625F, EnumHandSide.RIGHT);
        GlStateManager.translate(0f, -0.2f, 0f);
        renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
        renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
        GlStateManager.popMatrix();

        // Render left cone
        GlStateManager.pushMatrix();
        model.postRenderArm(0.0625F, EnumHandSide.LEFT);
        GlStateManager.translate(0f, -0.2f, 0f);
        renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
        renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
        GlStateManager.popMatrix();

        // Render head cone
        GlStateManager.pushMatrix();
        GlStateManager.translate(0f, 0.3f, 0f);
        GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);
        renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
        renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
        GlStateManager.popMatrix();

        // Check which slightly larger model to use
        ModelPlayer playerModel = ((AbstractClientPlayer) entityPlayer).getSkinType().equals("slim") ? playerModelAlex : playerModelSteve;

        // Define which parts are glowing
        playerModel.bipedBody.isHidden = playerModel.bipedLeftLeg.isHidden = playerModel.bipedRightLeg.isHidden = playerModel.bipedBodyWear.isHidden = playerModel.bipedHeadwear.isHidden = playerModel.bipedLeftLegwear.isHidden = playerModel.bipedRightLegwear.isHidden = false;

        // Copy model attributes from the real player model
        playerModel.setModelAttributes(model);

        // Render glowing overlay
        GlStateManager.color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 1);
        playerModel.render(entityPlayer, v, v1, v3, v4, v5, v6);

        // Undo state manager changes
        RenderUtil.restoreLightMap();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(255, 255, 255, 255);
        GlStateManager.enableTexture2D();
        GlStateManager.popAttrib();
    }


    //Renders the fiery cones
    public void renderCone(EntityPlayer entityPlayer, float scale, float scale2, Color color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        for (int i = 0; i < 8; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(1.0f, 1.0f, 0.65f);
            vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
            vertexBuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(0.0D, scale2, 1.0F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
