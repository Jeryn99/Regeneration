package me.swirtzly.regeneration.client.rendering.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.client.rendering.layers.HandsLayer;
import me.swirtzly.regeneration.client.rendering.layers.RegenerationLayer;
import me.swirtzly.regeneration.client.rendering.model.TimelordGuardModel;
import me.swirtzly.regeneration.client.rendering.model.TimelordModel;
import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import me.swirtzly.regeneration.common.item.GunItem;
import me.swirtzly.regeneration.util.common.PlayerUtil;
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
 * Created by Swirtzly
 * on 03/05/2020 @ 19:02
 */
public class TimelordRenderer extends LivingRenderer< TimelordEntity, BipedModel< TimelordEntity > > {

    public static EntityModel< TimelordEntity > mainModel = new TimelordModel();
    public static EntityModel< TimelordEntity > councilModel = new TimelordModel();
    public static EntityModel< TimelordEntity > guardModel = new TimelordGuardModel();
    public static PlayerModel< TimelordEntity > bipedModel = new PlayerModel<>(-0.25F, true);

    public static ResourceLocation TIMELORD = new ResourceLocation(Regeneration.MODID, "textures/entity/timelords/timelord/timelord_villager.png");


    public static HashMap< UUID, ResourceLocation > TIMELORDS = new HashMap<>();

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
            TIMELORDS.remove(timelordEntity.getUniqueID());
        }

        if (TIMELORDS.containsKey(timelordEntity.getUniqueID())) {
            return TIMELORDS.get(timelordEntity.getUniqueID());
        }

        NativeImage bufferedImage = SkinManipulation.decodeToImage(data.getEncodedSkin());
        if (bufferedImage == null) {
            return DefaultPlayerSkin.getDefaultSkinLegacy();
        }
        ResourceLocation location = Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("timelord_", new DynamicTexture(bufferedImage));
        TIMELORDS.put(timelordEntity.getUniqueID(), location);
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

        entityModel = (BipedModel< TimelordEntity >) mainModel;

        boolean flag = this.isVisible(entitylivingbaseIn);
        boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getInstance().player);
        if (flag || flag1) {
            if (!this.bindEntityTexture(entitylivingbaseIn)) {
                return;
            }

            if (flag1) {
                GlStateManager.setProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }


            if (!entitylivingbaseIn.isVillagerModel()) {
                Minecraft.getInstance().getTextureManager().bindTexture(TimelordRenderer.getTimelordFace(entitylivingbaseIn));
                bipedModel.isChild = false;


                for (RendererModel rendererModel : bipedModel.boxList) {
                    rendererModel.isHidden = true;
                }

                bipedModel.bipedHead.isHidden = false;
                bipedModel.bipedRightArm.isHidden = false;
                bipedModel.bipedLeftArm.isHidden = false;
                bipedModel.bipedBody.isHidden = false;
                bipedModel.bipedBodyWear.isHidden = false;
                bipedModel.bipedHeadwear.isHidden = !RegenConfig.CLIENT.renderTimelordHeadwear.get();

                bipedModel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

                bipedModel.rightArmPose = entityModel.rightArmPose;
                bipedModel.leftArmPose = entityModel.leftArmPose;
                bipedModel.bipedRightArm.copyModelAngles(entityModel.bipedRightArm);
                bipedModel.bipedLeftArm.copyModelAngles(entityModel.bipedLeftArm);
                bipedModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            }

            Minecraft.getInstance().getTextureManager().bindTexture(getEntityTexture(entitylivingbaseIn));
            entityModel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            entityModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            if (flag1) {
                GlStateManager.unsetProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(TimelordEntity entity) {
        switch (entity.getTimelordType()) {
            case COUNCIL:
                return new ResourceLocation(Regeneration.MODID, "textures/entity/timelords/timelord/timelord_council.png");
            case GUARD:
                return new ResourceLocation(Regeneration.MODID, "textures/entity/timelords/guards/timelord_guard.png");
        }
        return null;
    }
}
