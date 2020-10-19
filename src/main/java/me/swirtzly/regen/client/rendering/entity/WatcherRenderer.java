package me.swirtzly.regen.client.rendering.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.client.rendering.types.RenderTypes;
import me.swirtzly.regen.common.entities.WatcherEntity;
import me.swirtzly.regen.util.RConstants;
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

    public WatcherRenderer(EntityRendererManager p_i232471_1_, PlayerModel<WatcherEntity> p_i232471_2_, float p_i232471_3_, float p_i232471_4_, float p_i232471_5_, float p_i232471_6_) {
        super(p_i232471_1_, p_i232471_2_, p_i232471_3_, p_i232471_4_, p_i232471_5_, p_i232471_6_);
    }

    @Override
    public void render(WatcherEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    protected RenderType func_230496_a_(WatcherEntity watcherEntity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return RenderTypes.getEntityCutout(getEntityTexture(watcherEntity));
    }

    @Override
    public ResourceLocation getEntityTexture(WatcherEntity entity) {
        return TEXTURE;
    }
}
