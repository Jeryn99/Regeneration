package me.suff.mc.regen.client.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import me.suff.mc.regen.client.rendering.model.RModels;
import me.suff.mc.regen.client.skin.SkinHandler;
import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.tiles.BioContainerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;

import static me.suff.mc.regen.util.RegenUtil.round;

/* Created by Craig on 05/03/2021 */
public class JarTileRender implements BlockEntityRenderer<BioContainerBlockEntity> {

    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    public static HashMap<BioContainerBlockEntity, ResourceLocation> TEXTURES = new HashMap<>();
    private final ModelPart alexArm;
    private final ModelPart steveArm;

    public JarTileRender(BlockEntityRendererProvider.Context context) {
        alexArm = context.bakeLayer(RModels.ARM_ALEX);
        steveArm = context.bakeLayer(RModels.ARM_STEVE);
    }

    @Override
    public void render(BioContainerBlockEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

        if (tileEntityIn.getHand().getItem() instanceof HandItem && !tileEntityIn.isValid(BioContainerBlockEntity.Action.CREATE)) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5D, 1.5, 0.5D);
            matrixStackIn.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrixStackIn.last().pose();
            Font fontrenderer = Minecraft.getInstance().font;
            float f2 = (float) (-fontrenderer.width(new TranslatableComponent(String.valueOf(round(tileEntityIn.getLindos(), 2)))) / 2);
            fontrenderer.drawInBatch(new TranslatableComponent(String.valueOf(round(tileEntityIn.getLindos(), 2))), f2, (float) 1, -1, false, matrix4f, bufferIn, false, 0, combinedLightIn);
            matrixStackIn.popPose();
        }


        if (tileEntityIn.isUpdateSkin()) {
            TEXTURES.remove(tileEntityIn);
        }

        if (tileEntityIn.getHand().getItem() instanceof HandItem) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5D, 1.5, 0.5D);
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180));
            boolean isAlex = HandItem.isAlex(tileEntityIn.getHand());
            ModelPart mainModel = isAlex ? alexArm : steveArm;
            mainModel.render(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(getOrCreateTexture(tileEntityIn))), combinedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            matrixStackIn.popPose();
        } else {
            TEXTURES.remove(tileEntityIn);
        }

    }

    public ResourceLocation getOrCreateTexture(BioContainerBlockEntity tileEntityHandInJar) {

        if (!tileEntityHandInJar.getHand().getOrCreateTag().contains("skin")) {
            boolean isAlex = HandItem.isAlex(tileEntityHandInJar.getHand());
            return isAlex ? TEXTURE_ALEX : TEXTURE_STEVE;
        }

        if (!TEXTURES.containsKey(tileEntityHandInJar)) {
            NativeImage image = SkinHandler.genSkinNative(HandItem.getSkin(tileEntityHandInJar.getHand()));
            ResourceLocation res = Minecraft.getInstance().getTextureManager().register("hand_", new DynamicTexture(image));
            TEXTURES.put(tileEntityHandInJar, res);
            return res;
        }
        return TEXTURES.get(tileEntityHandInJar);
    }

    private void add(Fluid fluid, BlockAndTintGetter lightReader, BlockPos posIn, VertexConsumer renderer, PoseStack stack, float x, float y, float z, float u, float v) {
        int i = fluid.getAttributes().getColor(lightReader, posIn);
        float alpha = (float) (i >> 24 & 255) / 255.0F;
        float r = (float) (i >> 16 & 255) / 255.0F;
        float g = (float) (i >> 8 & 255) / 255.0F;
        float b = (float) (i & 255) / 255.0F;
        int j = getCombinedAverageLight(lightReader, posIn);
        renderer.vertex(stack.last().pose(), x, y, z)
                .color(r, g, b, alpha)
                .uv(u, v)
                .uv2(j & 0xffff, j >> 16 & 0xffff)
                .normal(1, 0, 0)
                .endVertex();
    }

    private int getCombinedAverageLight(BlockAndTintGetter lightReaderIn, BlockPos posIn) {
        int i = LevelRenderer.getLightColor(lightReaderIn, posIn);
        int j = LevelRenderer.getLightColor(lightReaderIn, posIn.above());
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (Math.max(k, l)) | (Math.max(i1, j1)) << 16;
    }

}
