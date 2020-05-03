package me.swirtzly.regeneration.client.rendering.entity;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.entity.TimelordModel;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by Swirtzly
 * on 03/05/2020 @ 19:02
 */
public class TimelordRenderer extends LivingRenderer<TimelordEntity, TimelordModel> {

    public TimelordRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new TimelordModel(), 0.1F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(TimelordEntity entity) {
        return new ResourceLocation(RegenerationMod.MODID, "textures/entity/timelord.png");
    }
}
