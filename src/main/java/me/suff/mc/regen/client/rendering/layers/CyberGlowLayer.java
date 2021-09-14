package me.suff.mc.regen.client.rendering.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import me.suff.mc.regen.client.rendering.model.CybermanModel;
import me.suff.mc.regen.common.entities.Cyberman;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class CyberGlowLayer extends RenderLayer<Cyberman, CybermanModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RConstants.MODID, "textures/entity/cyberman_glow.png");


    public CyberGlowLayer(RenderLayerParent<Cyberman, CybermanModel> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, Cyberman p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        // VertexConsumer vertexconsumer = p_117350_.getBuffer(RenderType.eyes(TEXTURE));
        // this.getParentModel().renderToBuffer(p_117349_, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }


}
