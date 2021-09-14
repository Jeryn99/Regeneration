package me.suff.mc.regen.client.rendering.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.client.rendering.model.CybermanModel;
import me.suff.mc.regen.client.rendering.model.ModifiedPlayerModel;
import me.suff.mc.regen.client.rendering.model.RModels;
import me.suff.mc.regen.common.entities.Cyberman;
import me.suff.mc.regen.common.entities.Timelord;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CyberGlowLayer extends RenderLayer<Cyberman, CybermanModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RConstants.MODID, "textures/entity/cyberman.png");
    private static final CybermanModel cyberModel = new CybermanModel(Minecraft.getInstance().getEntityModels().bakeLayer(RModels.CYBERMAN_GLOW));

    public CyberGlowLayer(RenderLayerParent<Cyberman, CybermanModel> p_117346_) {
        super(p_117346_);
    }


    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Cyberman cyberman, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        cyberModel.prepareMobModel(cyberman, limbSwing, limbSwingAmount, partialTicks);
        cyberModel.setAllVisible(false);
        cyberModel.head.visible = true;
        cyberModel.hat.visible = false;
        cyberModel.rightArm.visible = false;
        cyberModel.leftArm.visible = false;
        cyberModel.body.visible = true;
        cyberModel.young = false;

        VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.eyes(TEXTURE));
        cyberModel.setupAnim(cyberman, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        cyberModel.renderToBuffer(matrixStackIn, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1.0F);

    }
}
