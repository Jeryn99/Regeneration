package me.suff.mc.regen.client.rendering.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.suff.mc.regen.client.rendering.types.RenderTypes;
import me.suff.mc.regen.common.entities.WatcherEntity;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class WatcherRenderer extends HumanoidMobRenderer<WatcherEntity, PlayerModel<WatcherEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RConstants.MODID, "textures/entity/watcher.png");

    public WatcherRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new PlayerModel<>(0.5F, true), 0);
    }

    @Override
    public void render(WatcherEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
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
