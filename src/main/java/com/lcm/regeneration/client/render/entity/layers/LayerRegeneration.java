package com.lcm.regeneration.client.render.entity.layers;

import com.lcm.regeneration.Regeneration;
import com.lcm.regeneration.common.capability.CapabilityRegeneration;
import com.lcm.regeneration.common.capability.IRegeneration;
import com.lcm.regeneration.util.LimbManipulationUtil;
import lucraft.mods.lucraftcore.util.helper.LCRenderHelper;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nictogen on 3/16/18.
 */
@Mod.EventBusSubscriber @SideOnly(Side.CLIENT) public class LayerRegeneration implements LayerRenderer<EntityPlayer> {
    private static final ModelPlayer playerModelLargeArms = new ModelPlayer(0.1F, false);
    private static final ModelPlayer playerModelSmallArms = new ModelPlayer(0.1F, true);
    private static final ResourceLocation REGEN_TEXTURE = new ResourceLocation(Regeneration.MODID, "textures/entity/regen.png");
    private static ArrayList<EntityPlayer> layersAddedTo = new ArrayList<>();
    private static World lastWorld;
    private RenderPlayer playerRenderer;
    static int arm_shake = 0;

    static Random RAND = new Random();

    public LayerRegeneration(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }

    public void doRenderLayer(EntityPlayer player, float p_177169_2_, float p_177169_3_, float p_177169_4_, float p_177169_5_, float p_177169_6_, float p_177169_7_, float p_177169_8_) {

        if (player.hasCapability(CapabilityRegeneration.TIMELORD_CAP, null)
                && player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null).isTimelord()) {
            IRegeneration capability = player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);

            if (capability.getState() == CapabilityRegeneration.RegenerationState.NONE)
                return;

            NBTTagCompound style = capability.getStyle();

            if (style.getBoolean("textured"))
                renderTexturedEffect(this.playerRenderer, capability, player, p_177169_2_, p_177169_3_, p_177169_4_, p_177169_5_, p_177169_6_, p_177169_7_, p_177169_8_);
            else
                renderEffect(this.playerRenderer, capability, player, p_177169_2_, p_177169_3_, p_177169_4_, p_177169_5_, p_177169_6_, p_177169_7_, p_177169_8_);

        }

    }

    private void renderTexturedEffect(RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float v, float v1, float v2, float v3, float v4, float v5, float v6) {

        ModelBiped model = (ModelBiped) renderLivingBase.getMainModel();

        // State manager changes
        GlStateManager.pushAttrib();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(false);
        LCRenderHelper.setLightmapTextureCoords(175, 175);

        NBTTagCompound style = capability.getStyle();
        Color primaryColor = new Color(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
        Color secondaryColor = new Color(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));

        float primaryScale = capability.getRegenTicks() / 40f;
        float secondaryScale = capability.getRegenTicks() / 70f;

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float g = (capability.getRegenTicks() + v2) * 0.01F;
        GlStateManager.translate(0.0F, g, g);
        GlStateManager.matrixMode(5888);

        // Render right cone
        GlStateManager.pushMatrix();
        model.postRenderArm(0.0625F, EnumHandSide.RIGHT);
        GlStateManager.translate(0f, -0.2f, 0f);
        GlStateManager.rotate(capability.getRegenTicks() * 2 + v2 * 2, 0.0F, 1.0F, 0.0f);
        renderLivingBase.bindTexture(REGEN_TEXTURE);
        renderTexturedCone(entityPlayer, primaryScale, primaryScale, primaryColor);
        renderTexturedCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
        GlStateManager.popMatrix();

        // Render left cone
        GlStateManager.pushMatrix();
        model.postRenderArm(0.0625F, EnumHandSide.LEFT);
        GlStateManager.translate(0f, -0.2f, 0f);
        GlStateManager.rotate(capability.getRegenTicks() * 2 + v2 * 2, 0.0F, 1.0F, 0.0f);
        renderTexturedCone(entityPlayer, primaryScale, primaryScale, primaryColor);
        renderTexturedCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
        GlStateManager.popMatrix();

        // Render head cone
        GlStateManager.pushMatrix();
        GlStateManager.translate(0f, 0.3f, 0f);
        GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(capability.getRegenTicks() * 2 + v2 * 2, 0.0F, 1.0F, 0.0f);
        renderTexturedCone(entityPlayer, primaryScale, primaryScale, primaryColor);
        renderTexturedCone(entityPlayer, secondaryScale, secondaryScale * 2f, secondaryColor);
        GlStateManager.popMatrix();

        // Check which slightly larger model to use
        ModelPlayer playerModel = ((AbstractClientPlayer) entityPlayer).getSkinType().equals("slim") ? playerModelSmallArms : playerModelLargeArms;

        // Copy model attributes from the real player model
        playerModel.setModelAttributes(model);

        // Undo state manager changes
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(255, 255, 255, 255);
        GlStateManager.enableTexture2D();
        GlStateManager.popAttrib();

        // Render glowing overlay
        renderLivingBase.bindTexture(REGEN_TEXTURE);

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = capability.getRegenTicks() * 3 + v2;
        GlStateManager.translate(0.0F, -f * 0.01F, 0.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.enableBlend();

        GlStateManager.color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 0.25F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);

        playerModel.bipedBody.isHidden = playerModel.bipedLeftLeg.isHidden = playerModel.bipedRightLeg.isHidden = playerModel.bipedBodyWear.isHidden = playerModel.bipedLeftLegwear.isHidden = playerModel.bipedRightLegwear.isHidden = true;

        playerModel.bipedHead.isHidden = playerModel.bipedHeadwear.isHidden = true;
        playerModel.bipedLeftArmwear.isHidden = playerModel.bipedLeftArm.isHidden = playerModel.bipedRightArmwear.isHidden = playerModel.bipedRightArm.isHidden = false;
        playerModel.render(entityPlayer, v, v1, v3, v4, v5, v6);

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, f * 0.01F, 0.0F);
        GlStateManager.matrixMode(5888);

        playerModel.bipedHead.isHidden = playerModel.bipedHeadwear.isHidden = false;
        playerModel.bipedLeftArmwear.isHidden = playerModel.bipedLeftArm.isHidden = playerModel.bipedRightArmwear.isHidden = playerModel.bipedRightArm.isHidden = true;
        playerModel.render(entityPlayer, v, v1, v3, v4, v5, v6);

        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.disableBlend();
        LCRenderHelper.restoreLightmapTextureCoords();
    }

    private void renderTexturedCone(EntityPlayer entityPlayer, float scale, float scale2, Color color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        for (int i = 0; i < 8; i++) {
            float tex = 0.5F;
            GlStateManager.pushMatrix();
            GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(1.0f, 1.0f, 0.65f);
            vertexBuffer.begin(6, DefaultVertexFormats.POSITION_TEX_COLOR);
            vertexBuffer.pos(0.0D, 0.0D, 0.0D).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), 50).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).tex(0, tex).color(color.getRed(), color.getGreen(), color.getBlue(), 50).endVertex();
            vertexBuffer.pos(0.266D * scale, scale, -0.5F * scale).tex(tex, tex).color(color.getRed(), color.getGreen(), color.getBlue(), 50).endVertex();
            vertexBuffer.pos(0.0D, scale2, 1.0F * scale).tex(tex, 0).color(color.getRed(), color.getGreen(), color.getBlue(), 50).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), 50).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }

    private void renderEffect(RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float v, float v1, float v2, float v3, float v4, float v5, float v6) {
        ModelBiped model = (ModelBiped) renderLivingBase.getMainModel();

        // State manager changes
        GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(false);
        LCRenderHelper.setLightmapTextureCoords(65, 65);

        NBTTagCompound style = capability.getStyle();
        Color primaryColor = new Color(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
        Color secondaryColor = new Color(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));

        float primaryScale = capability.getRegenTicks() / 40f;
        float secondaryScale = capability.getRegenTicks() / 70f;
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
        ModelPlayer playerModel = ((AbstractClientPlayer) entityPlayer).getSkinType().equals("slim") ? playerModelSmallArms : playerModelLargeArms;

        // Define which parts are glowing
        playerModel.bipedBody.isHidden = playerModel.bipedLeftLeg.isHidden = playerModel.bipedRightLeg.isHidden = playerModel.bipedBodyWear.isHidden = playerModel.bipedHeadwear.isHidden = playerModel.bipedLeftLegwear.isHidden = playerModel.bipedRightLegwear.isHidden = false;

        // Copy model attributes from the real player model
        playerModel.setModelAttributes(model);

        // Render glowing overlay
        GlStateManager.color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 1);
        playerModel.render(entityPlayer, v, v1, v3, v4, v5, v6);

        // Undo state manager changes
        LCRenderHelper.restoreLightmapTextureCoords();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(255, 255, 255, 255);
        GlStateManager.enableTexture2D();
        GlStateManager.popAttrib();
    }

    private void renderCone(EntityPlayer entityPlayer, float scale, float scale2, Color color) {
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

    @SubscribeEvent public static void onRenderPlayerPre(RenderPlayerEvent.Pre e) {
        IRegeneration handler = e.getEntityPlayer().getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
        if (handler != null && handler.isTimelord() && handler.getState() != CapabilityRegeneration.RegenerationState.NONE) {
            arm_shake = RAND.nextInt(7);
            LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.LEFT_ARM).setAngles(0, 0, -75 + arm_shake);
            LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.RIGHT_ARM).setAngles(0, 0, 75 + arm_shake);
            LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.HEAD).setAngles(-50, 0, 0);
            LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.LEFT_LEG).setAngles(0, 0, -10);
            LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.RIGHT_LEG).setAngles(0, 0, 10);
        }
    }

    @SubscribeEvent public static void onRenderPlayerPost(RenderPlayerEvent.Post e) {
        if(lastWorld != e.getEntityPlayer().world){
            lastWorld = e.getEntityPlayer().world;
            layersAddedTo.clear();
        }
        if (!layersAddedTo.contains(e.getEntityPlayer())) {
            layersAddedTo.add(e.getEntityPlayer());
            e.getRenderer().addLayer(new LayerRegeneration(e.getRenderer()));
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}