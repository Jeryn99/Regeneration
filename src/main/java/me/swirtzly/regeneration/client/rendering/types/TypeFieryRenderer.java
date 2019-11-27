package me.swirtzly.regeneration.client.rendering.types;

import me.swirtzly.regeneration.client.animation.AnimationContext;
import me.swirtzly.regeneration.client.animation.RenderCallbackEvent;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.client.skinhandling.SkinInfo;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.TypeFiery;
import me.swirtzly.regeneration.common.types.TypeHandler;
import me.swirtzly.regeneration.util.RenderUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderPlayerEvent;

import javax.annotation.Nullable;

import static me.swirtzly.regeneration.client.animation.AnimationHandler.copyAndReturn;
import static me.swirtzly.regeneration.client.rendering.layers.LayerRegeneration.modelAlex;
import static me.swirtzly.regeneration.client.rendering.layers.LayerRegeneration.modelSteve;

public class TypeFieryRenderer extends ATypeRenderer<TypeFiery> {

    public static final TypeFieryRenderer INSTANCE = new TypeFieryRenderer();

    private TypeFieryRenderer() {
    }

    public static void renderCone(EntityPlayer entityPlayer, float scale, float scale2, Vec3d color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();

        for (int i = 0; i < 8; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(1.0f, 1.0f, 0.65f);
            vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
            vertexBuffer.pos(0.0D, 0.0D, 0.0D).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
            vertexBuffer.pos(0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
            vertexBuffer.pos(0.0D, scale2, 1.0F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }

    public static void renderOverlay(EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, @Nullable ModelBase base) {
        GlStateManager.pushMatrix();
        RenderUtil.setLightmapTextureCoords(240, 240);
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 1);
        Vec3d color = CapabilityRegeneration.getForPlayer(entityPlayer).getPrimaryColor();
        float opacity = MathHelper.clamp(MathHelper.sin((entityPlayer.ticksExisted + partialTicks) / 10F) * 0.1F + 0.1F, 0.11F, 1F);
        GlStateManager.color((float) color.x, (float) color.y, (float) color.z, opacity);

        if (base == null) {
            if (SkinChangingHandler.getSkinType((AbstractClientPlayer) entityPlayer, false) == SkinInfo.SkinType.STEVE) {
                modelSteve.isChild = false;
                modelSteve.render(entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            } else {
                modelAlex.isChild = false;
                modelAlex.render(entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        } else {
            GlStateManager.scale(1.1, 1.1, 1.);
            base.render(entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        RenderUtil.restoreLightMap();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void renderConeAtArms(EntityPlayer player) {
        IRegeneration capability = CapabilityRegeneration.getForPlayer(player);
        double x = TypeHandler.getTypeInstance(capability.getType()).getAnimationProgress(capability);
        double p = 109.89010989010987; // see the wiki for the explanation of these "magic" numbers
        double r = 0.09890109890109888;
        double f = p * Math.pow(x, 2) - r;
        float cf = MathHelper.clamp((float) f, 0F, 1F);
        float primaryScale = cf * 4F;
        float secondaryScale = cf * 6.4F;

        NBTTagCompound style = capability.getStyle();
        Vec3d primaryColor = new Vec3d(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
        Vec3d secondaryColor = new Vec3d(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));


        // State manager changes
        GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        GlStateManager.depthMask(true);
        RenderUtil.setLightmapTextureCoords(65, 65);

        if (capability.isSyncingToJar()) {
            GlStateManager.rotate(-20, 1, 0, 0);
        }

        renderCone(player, primaryScale, primaryScale, primaryColor);
        renderCone(player, secondaryScale, secondaryScale * 1.5f, secondaryColor);

        // Undo state manager changes
        RenderUtil.restoreLightMap();
        GlStateManager.depthMask(false);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(255, 255, 255, 255);
        GlStateManager.enableTexture2D();
        GlStateManager.popAttrib();
    }

    @Override
    public void onRenderRegeneratingPre(TypeFiery type, RenderPlayerEvent.Pre ev, IRegeneration cap) {
    }

    @Override
    protected void onRenderRegeneratingPost(TypeFiery type, RenderPlayerEvent.Post event, IRegeneration capability) {

    }

    @Override
    public boolean onAnimateRegen(AnimationContext animationContext) {
        EntityPlayer player = animationContext.getEntityPlayer();
        IRegeneration data = CapabilityRegeneration.getForPlayer(player);
        ModelBiped playerModel = animationContext.getModelBiped();
        double animationProgress = data.getAnimationTicks();
        double arm_shake = player.getRNG().nextDouble();

        float armRot = (float) animationProgress * 1.5F;
        float headRot = (float) animationProgress * 1.5F;

        if (armRot > 90) {
            armRot = 90;
        }

        if (headRot > 45) {
            headRot = 45;
        }


        //ARMS
        playerModel.bipedLeftArm.rotateAngleY = 0;
        playerModel.bipedRightArm.rotateAngleY = 0;

        playerModel.bipedLeftArm.rotateAngleX = 0;
        playerModel.bipedRightArm.rotateAngleX = 0;

        playerModel.bipedLeftArm.rotateAngleZ = (float) -Math.toRadians(armRot + arm_shake);
        playerModel.bipedRightArm.rotateAngleZ = (float) Math.toRadians(armRot + arm_shake);

        //BODY
        playerModel.bipedBody.rotateAngleX = 0;
        playerModel.bipedBody.rotateAngleY = 0;
        playerModel.bipedBody.rotateAngleZ = 0;


        //LEGS
        playerModel.bipedLeftLeg.rotateAngleY = 0;
        playerModel.bipedRightLeg.rotateAngleY = 0;

        playerModel.bipedLeftLeg.rotateAngleX = 0;
        playerModel.bipedRightLeg.rotateAngleX = 0;

        playerModel.bipedLeftLeg.rotateAngleZ = (float) -Math.toRadians(5);
        playerModel.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(5);

        playerModel.bipedHead.rotateAngleX = (float) Math.toRadians(-headRot);
        playerModel.bipedHead.rotateAngleY = (float) Math.toRadians(0);
        playerModel.bipedHead.rotateAngleZ = (float) Math.toRadians(0);

        return copyAndReturn(playerModel, true);
    }

    @Deprecated //This duplicated code needs sorted asap
    @Override
    public void renderHand(EntityPlayer player, EnumHandSide handSide, RenderLivingBase<?> render) {
        renderConeAtArms(player);
    }

    @Override
    public void onRenderCallBack(RenderCallbackEvent event) {

    }

    @Override
    public void onRenderLayer(TypeFiery type, RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        // State manager changes
        GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(true);
        RenderUtil.setLightmapTextureCoords(65, 65);

        NBTTagCompound style = capability.getStyle();
        Vec3d primaryColor = new Vec3d(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
        Vec3d secondaryColor = new Vec3d(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));

        double x = type.getAnimationProgress(capability);
        double p = 109.89010989010987; // see the wiki for the explanation of these "magic" numbers
        double r = 0.09890109890109888;
        double f = p * Math.pow(x, 2) - r;

        float cf = MathHelper.clamp((float) f, 0F, 1F);
        float primaryScale = cf * 4F;
        float secondaryScale = cf * 6.4F;

        // Render head cone
        GlStateManager.pushMatrix();

        if (renderLivingBase.getMainModel() instanceof ModelPlayer) {
            ModelPlayer player = (ModelPlayer) renderLivingBase.getMainModel();
            player.bipedHead.postRender(0.0625F);
        }

        //GlStateManager.translate(0f, 0.1f, 0f);
        GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);

        renderCone(entityPlayer, primaryScale / 1.6F, primaryScale * .75F, primaryColor);
        renderCone(entityPlayer, secondaryScale / 1.6F, secondaryScale / 1.5F, secondaryColor);
        GlStateManager.popMatrix();

        if (!capability.isSyncingToJar()) {
            // Render glowing overlay
            renderOverlay(entityPlayer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, null);
        }
        // Undo state manager changes
        RenderUtil.restoreLightMap();
        GlStateManager.depthMask(false);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(255, 255, 255, 255);
        GlStateManager.enableTexture2D();
        GlStateManager.popAttrib();
    }

}
