package me.suff.mc.regen.client.rendering.entity;

import me.suff.mc.regen.client.rendering.model.OmegaModel;
import me.suff.mc.regen.common.entities.OmegaEntity;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

/* Created by Craig on 31/03/2021 */
public class RenderOmega extends LivingRenderer {

    public RenderOmega(EntityRendererManager rendererManager) {
        super(rendererManager, new OmegaModel(), 1);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity p_110775_1_) {
        return new ResourceLocation(RConstants.MODID, "textures/entity/omega.png");
    }

}
