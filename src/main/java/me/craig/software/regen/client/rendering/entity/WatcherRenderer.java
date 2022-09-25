package me.craig.software.regen.client.rendering.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.craig.software.regen.client.rendering.types.RenderTypes;
import me.craig.software.regen.common.entities.WatcherEntity;
import me.craig.software.regen.util.RConstants;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class WatcherRenderer extends BipedRenderer<WatcherEntity, PlayerModel<WatcherEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RConstants.MODID, "textures/entity/watcher.png");

    public WatcherRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PlayerModel<>(0.5F, true), 0);
    }

    @Override
    public void render(WatcherEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(WatcherEntity watcherEntity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return RenderTypes.entityCutout(getTextureLocation(watcherEntity));
    }

    @Override
    public ResourceLocation getTextureLocation(WatcherEntity entity) {
        return TEXTURE;
    }
}
