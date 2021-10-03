package me.suff.mc.regen.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.suff.mc.regen.client.rendering.model.ArmModel;
import me.suff.mc.regen.client.rendering.model.ContainerModel;
import me.suff.mc.regen.client.skin.SkinHandler;
import me.suff.mc.regen.common.block.JarBlock;
import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.tiles.JarTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockDisplayReader;

import java.util.HashMap;

import static me.suff.mc.regen.util.RegenUtil.round;

/* Created by Craig on 05/03/2021 */
public class JarTileRender extends TileEntityRenderer<JarTile> {

    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    public static HashMap<JarTile, ResourceLocation> TEXTURES = new HashMap<>();

    ArmModel steveArmModel = new ArmModel(false);
    ArmModel armModel = new ArmModel(true);
    EntityModel mainModel = new ArmModel(true);
    ContainerModel containerModel = new ContainerModel();

    public JarTileRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(JarTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        matrixStackIn.pushPose();
        BlockState blockstate = tileEntityIn.getBlockState();
        float rotation = 22.5F * (float) blockstate.getValue(JarBlock.ROTATION);

        // Render remaining lindos amount
        if (tileEntityIn.getHand().getItem() instanceof HandItem && !tileEntityIn.isValid(JarTile.Action.CREATE) && Minecraft.renderNames()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5D, 1.2, 0.5D);
            matrixStackIn.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrixStackIn.last().pose();
            FontRenderer fontrenderer = Minecraft.getInstance().font;
            float f2 = (float) (-fontrenderer.width(new TranslationTextComponent(String.valueOf(round(tileEntityIn.getLindos(), 2)))) / 2);
            fontrenderer.drawInBatch(new TranslationTextComponent(String.valueOf(round(tileEntityIn.getLindos(), 2))), f2, (float) 1, -1, false, matrix4f, bufferIn, false, 0, combinedLightIn);
            matrixStackIn.popPose();
        }


        if (tileEntityIn.isUpdateSkin()) {
            TEXTURES.remove(tileEntityIn);
        }

        // Render Hand
        if (tileEntityIn.getHand().getItem() instanceof HandItem) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5D, -0.6, 0.5D);
            boolean isAlex = HandItem.isAlex(tileEntityIn.getHand());
            mainModel = isAlex ? armModel : steveArmModel;
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(rotation));
            matrixStackIn.scale(0.8F,0.8F,0.8F);
            matrixStackIn.translate(0,0.1F,0.04);
            mainModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(getOrCreateTexture(tileEntityIn))), combinedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            matrixStackIn.popPose();
        } else {
            TEXTURES.remove(tileEntityIn);
        }

        // Render Block
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(0.5,-1.5,0.5);
        matrixStackIn.translate(-1,0,-1);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(rotation));
        containerModel.Lid.xRot = -(tileEntityIn.getOpenAmount() * ((float) Math.PI / 3F));;
        containerModel.Lid.yRot = 0;
        containerModel.Lid.zRot = 0;
        containerModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(ContainerModel.CONTAINER_TEXTURE)), combinedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        matrixStackIn.popPose();


        matrixStackIn.popPose();

    }

    public ResourceLocation getOrCreateTexture(JarTile tileEntityHandInJar) {

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

    private void add(Fluid fluid, IBlockDisplayReader lightReader, BlockPos posIn, IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v) {
        int i = fluid.getFluid().getAttributes().getColor(lightReader, posIn);
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

    private int getCombinedAverageLight(IBlockDisplayReader lightReaderIn, BlockPos posIn) {
        int i = WorldRenderer.getLightColor(lightReaderIn, posIn);
        int j = WorldRenderer.getLightColor(lightReaderIn, posIn.above());
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (Math.max(k, l)) | (Math.max(i1, j1)) << 16;
    }

}
