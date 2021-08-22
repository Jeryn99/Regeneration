package me.suff.mc.regen.client.rendering.types;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTypes extends RenderType {

    public static final RenderType REGEN_FLAMES = create(RConstants.MODID + ":laser", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
            .setTextureState(RenderStateShard.NO_TEXTURE)
            .setCullState(RenderStateShard.CULL)
            //TODO  .setAlphaState(DEFAULT_ALPHA)
            .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_SMOOTH_CUTOUT_SHADER)
            .setLightmapState(RenderStateShard.LIGHTMAP)
            .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
            .createCompositeState(true));

    public RenderTypes(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }


    public static RenderType getGlowing(ResourceLocation locationIn) {
        RenderStateShard.TextureStateShard textureState = new RenderStateShard.TextureStateShard(locationIn, false, false);
        return create(RConstants.MODID + ":glowing", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setTransparencyState(TRANSLUCENT_TRANSPARENCY)/*.setAlphaState(DEFAULT_ALPHA)*/.setCullState(NO_CULL).setOverlayState(OVERLAY).setTextureState(textureState)/*.setFogState(BLACK_FOG)*/.createCompositeState(false));
    }

    public static RenderType getGlowingTransparent(ResourceLocation locationIn) {
        RenderStateShard.TextureStateShard textureState = new RenderStateShard.TextureStateShard(locationIn, false, false);
        return create(RConstants.MODID + ":glowing_transparent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setShaderState(RENDERTYPE_ENTITY_ALPHA_SHADER).setCullState(RenderStateShard.CULL).setOverlayState(RenderStateShard.NO_OVERLAY).setTextureState(textureState)/*.setFogState(BLACK_FOG)*/.createCompositeState(true));
    }

    public static RenderType getEntityTranslucentHalfAlpha(ResourceLocation LocationIn, boolean outlineIn) {
        RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(LocationIn, false, false)).setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY).setShaderState(RENDERTYPE_ENTITY_ALPHA_SHADER)/*.setAlphaState(RenderStateShard.MIDWAY_ALPHA)*/.setCullState(CULL).setLightmapState(RenderStateShard.LIGHTMAP).setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(outlineIn);
        return create("entity_translucent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$state);
    }

}

