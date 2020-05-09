package me.swirtzly.regeneration.client.rendering.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.client.rendering.layers.HandsLayer;
import me.swirtzly.regeneration.client.rendering.layers.RegenerationLayer;
import me.swirtzly.regeneration.client.rendering.model.TimelordGuardModel;
import me.swirtzly.regeneration.client.rendering.model.TimelordModel;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by Swirtzly
 * on 03/05/2020 @ 19:02
 */
public class TimelordRenderer extends LivingRenderer<TimelordEntity, BipedModel<TimelordEntity>> {

    public static EntityModel<TimelordEntity> mainModel = new TimelordModel();
    public static EntityModel<TimelordEntity> councilModel = new TimelordModel();
    public static EntityModel<TimelordEntity> guardModel = new TimelordGuardModel();

    public TimelordRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new BipedModel<>(), 0.1F);
        addLayer(new RegenerationLayer(this));
        addLayer(new HandsLayer(this));
        addLayer(new HeldItemLayer<>(this));
        addLayer(new ArrowLayer<>(this));
        addLayer(new HeadLayer<>(this));
        addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
    }

    @Override
    protected void renderModel(TimelordEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {

        switch (entitylivingbaseIn.getTimelordType()){
            case GUARD:
                mainModel = guardModel;
                break;
            case COUNCIL:
                mainModel = councilModel;
                break;
        }

        entityModel = (BipedModel<TimelordEntity>) mainModel;

        boolean flag = this.isVisible(entitylivingbaseIn);
        boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getInstance().player);
        if (flag || flag1) {
            if (!this.bindEntityTexture(entitylivingbaseIn)) {
                return;
            }

            if (flag1) {
                GlStateManager.setProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
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
        return new ResourceLocation(Regeneration.MODID, "textures/entity/timelords/" + entity.getTimelordType().getName() + "/" + entity.getTimelordType().getName() + "_" + entity.getSkin() + ".png");
    }
}
