package me.suff.mc.regen.client.rendering.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import me.suff.mc.regen.RegenConfig;
import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.client.rendering.layers.HandsLayer;
import me.suff.mc.regen.client.rendering.layers.RegenerationLayer;
import me.suff.mc.regen.client.rendering.model.TimelordGuardModel;
import me.suff.mc.regen.client.rendering.model.TimelordModel;
import me.suff.mc.regen.client.skinhandling.SkinManipulation;
import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.entity.TimelordEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Craig
 * on 03/05/2020 @ 19:02
 */
public class TimelordRenderer extends LivingRenderer<TimelordEntity, BipedModel<TimelordEntity>> {

    public static EntityModel<TimelordEntity> mainModel = new TimelordModel();
    public static EntityModel<TimelordEntity> councilModel = new TimelordModel();
    public static EntityModel<TimelordEntity> guardModel = new TimelordGuardModel();
    public static PlayerModel<TimelordEntity> bipedModel = new PlayerModel<>(-0.25F, true);

    public static ResourceLocation TIMELORD = new ResourceLocation(Regeneration.MODID, "textures/entity/timelords/timelord/timelord_villager.png");


    public static HashMap<UUID, ResourceLocation> TIMELORDS = new HashMap<>();

    public TimelordRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new BipedModel<>(), 0.1F);
        addLayer(new RegenerationLayer(this));
        addLayer(new HandsLayer(this));
        addLayer(new HeldItemLayer<>(this));
        addLayer(new ArrowLayer<>(this));
        addLayer(new HeadLayer<>(this));
        addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
    }


    public static ResourceLocation getTimelordFace(TimelordEntity timelordEntity) {

        IRegen data = RegenCap.get(timelordEntity).orElseGet(null);

        if (timelordEntity.isVillagerModel()) {
            return TIMELORD;
        }

        if (data.getAnimationTicks() > 100) {
            TIMELORDS.remove(timelordEntity.getUUID());
        }

        if (TIMELORDS.containsKey(timelordEntity.getUUID())) {
            return TIMELORDS.get(timelordEntity.getUUID());
        }

        NativeImage bufferedImage = SkinManipulation.decodeToImage(data.getEncodedSkin());
        if (bufferedImage == null) {
            return DefaultPlayerSkin.getDefaultSkin();
        }
        ResourceLocation location = Minecraft.getInstance().getTextureManager().register("timelord_", new DynamicTexture(bufferedImage));
        TIMELORDS.put(timelordEntity.getUUID(), location);
        return location;

    }

    @Override
    protected void renderModel(TimelordEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        switch (entitylivingbaseIn.getTimelordType()) {
            case GUARD:
                mainModel = guardModel;
                break;
            case COUNCIL:
                mainModel = councilModel;
                break;
        }

        model = (BipedModel<TimelordEntity>) mainModel;

        boolean flag = this.isVisible(entitylivingbaseIn);
        boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleTo(Minecraft.getInstance().player);
        if (flag || flag1) {
            if (!this.bindTexture(entitylivingbaseIn)) {
                return;
            }

            if (flag1) {
                GlStateManager.setProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }


            if (!entitylivingbaseIn.isVillagerModel()) {
                Minecraft.getInstance().getTextureManager().bind(TimelordRenderer.getTimelordFace(entitylivingbaseIn));
                bipedModel.young = false;


                for (RendererModel rendererModel : bipedModel.cubes) {
                    rendererModel.neverRender = true;
                }

                bipedModel.head.neverRender = false;
                bipedModel.rightArm.neverRender = false;
                bipedModel.leftArm.neverRender = false;
                bipedModel.body.neverRender = false;
                bipedModel.jacket.neverRender = false;
                bipedModel.hat.neverRender = !RegenConfig.CLIENT.renderTimelordHeadwear.get();

                bipedModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

                bipedModel.rightArmPose = model.rightArmPose;
                bipedModel.leftArmPose = model.leftArmPose;
                bipedModel.rightArm.copyFrom(model.rightArm);
                bipedModel.leftArm.copyFrom(model.leftArm);
                bipedModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            }

            Minecraft.getInstance().getTextureManager().bind(getTextureLocation(entitylivingbaseIn));
            model.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            if (flag1) {
                GlStateManager.unsetProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureLocation(TimelordEntity entity) {
        switch (entity.getTimelordType()) {
            case COUNCIL:
                return new ResourceLocation(Regeneration.MODID, "textures/entity/timelords/timelord/timelord_council.png");
            case GUARD:
                return new ResourceLocation(Regeneration.MODID, "textures/entity/timelords/guards/timelord_guard.png");
        }
        return null;
    }
}
