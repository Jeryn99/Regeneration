package mc.craig.software.regen.client.rendering.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.client.rendering.types.RenderTypes;
import mc.craig.software.regen.common.entities.Watcher;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class WatcherRenderer extends HumanoidMobRenderer<Watcher, PlayerModel<Watcher>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RConstants.MODID, "textures/entity/watcher.png");

    public WatcherRenderer(EntityRendererProvider.Context entityRendererManager) {
        super(entityRendererManager, new PlayerModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER), false), 0.1F);
    }

    @Override
    public void render(@NotNull Watcher entityIn, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(@NotNull Watcher watcher, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return RenderTypes.entityCutout(getTextureLocation(watcher));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Watcher entity) {
        return TEXTURE;
    }
}
