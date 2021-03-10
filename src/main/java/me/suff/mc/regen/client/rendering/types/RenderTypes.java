package me.suff.mc.regen.client.rendering.types;

import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTypes extends RenderType {

    public static final RenderType REGEN_FLAMES = create(RConstants.MODID + ":laser", DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, GL11.GL_QUADS, 256, false, false, RenderType.State.builder()
            .setTextureState(RenderState.NO_TEXTURE)
            .setCullState(RenderState.CULL)
            .setAlphaState(DEFAULT_ALPHA)
            .setShadeModelState(RenderState.SMOOTH_SHADE)
            .setLightmapState(RenderState.LIGHTMAP)
            .setTransparencyState(RenderState.ADDITIVE_TRANSPARENCY)
            .createCompositeState(true));

    public RenderTypes(String name, VertexFormat vertexFormat, int drawMode, int bufferSize, boolean useDelegate, boolean needsSorting, Runnable setupTask, Runnable clearTask) {
        super(name, vertexFormat, drawMode, bufferSize, useDelegate, needsSorting, setupTask, clearTask);
    }

    public static RenderType getGlowing(ResourceLocation locationIn) {
        RenderState.TextureState textureState = new RenderState.TextureState(locationIn, false, false);
        return create(RConstants.MODID + ":glowing", DefaultVertexFormats.NEW_ENTITY, 7, 256, false, true, RenderType.State.builder().setTransparencyState(TRANSLUCENT_TRANSPARENCY).setAlphaState(DEFAULT_ALPHA).setCullState(NO_CULL).setOverlayState(OVERLAY).setTextureState(textureState).setFogState(BLACK_FOG).createCompositeState(false));
    }

    public static RenderType getGlowingTransparent(ResourceLocation locationIn) {
        RenderState.TextureState textureState = new RenderState.TextureState(locationIn, false, false);
        return create(RConstants.MODID + ":glowing_transparent", DefaultVertexFormats.NEW_ENTITY, 7, 256, false, true, RenderType.State.builder().setTransparencyState(RenderState.TRANSLUCENT_TRANSPARENCY).setAlphaState(RenderState.MIDWAY_ALPHA).setCullState(RenderState.CULL).setOverlayState(RenderState.NO_OVERLAY).setTextureState(textureState).setFogState(BLACK_FOG).createCompositeState(true));
    }

    public static RenderType getEntityTranslucentHalfAlpha(ResourceLocation LocationIn, boolean outlineIn) {
        RenderType.State rendertype$state = RenderType.State.builder().setTextureState(new RenderState.TextureState(LocationIn, false, false)).setTransparencyState(RenderState.LIGHTNING_TRANSPARENCY).setAlphaState(RenderState.MIDWAY_ALPHA).setCullState(CULL).setLightmapState(RenderState.LIGHTMAP).setOverlayState(RenderState.NO_OVERLAY).createCompositeState(outlineIn);
        return create("entity_translucent", DefaultVertexFormats.NEW_ENTITY, 7, 256, true, true, rendertype$state);
    }

}

