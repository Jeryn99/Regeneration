package me.swirtzly.regen.client.rendering.types;

import me.swirtzly.regen.util.RConstants;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTypes extends RenderType {

    public RenderTypes(String name, VertexFormat vertexFormat, int drawMode, int bufferSize, boolean useDelegate, boolean needsSorting, Runnable setupTask, Runnable clearTask) {
        super(name, vertexFormat, drawMode, bufferSize, useDelegate, needsSorting, setupTask, clearTask);
    }

    public static final RenderType LASER = makeType(RConstants.MODID + ":laser", DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, GL11.GL_QUADS, 256, false, false, RenderType.State.getBuilder()
            .texture(RenderState.NO_TEXTURE)
            .cull(RenderState.CULL_ENABLED)
            .alpha(DEFAULT_ALPHA)
            .transparency(RenderState.LIGHTNING_TRANSPARENCY)
            .build(true));

    public static RenderType getGlowing(ResourceLocation locationIn) {
        RenderState.TextureState textureState = new RenderState.TextureState(locationIn, false, false);
        return makeType(RConstants.MODID + ":glowing", DefaultVertexFormats.ENTITY, 7, 256, false, true, RenderType.State.getBuilder().transparency(TRANSLUCENT_TRANSPARENCY).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).overlay(OVERLAY_ENABLED).texture(textureState).fog(BLACK_FOG).build(false));
    }
}

