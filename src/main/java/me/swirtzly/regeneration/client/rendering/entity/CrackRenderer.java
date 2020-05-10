package me.swirtzly.regeneration.client.rendering.entity;

import me.swirtzly.regeneration.client.rendering.model.CrackModel;
import me.swirtzly.regeneration.common.entity.CrackEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by Swirtzly
 * on 10/05/2020 @ 22:42
 */
public class CrackRenderer extends EntityRenderer<CrackEntity> {

    private CrackModel crackModel = new CrackModel();

    public CrackRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(CrackEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        crackModel.render(entity, 0, 0, 0, 0, 0, 1);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(CrackEntity entity) {
        return null;
    }
}
