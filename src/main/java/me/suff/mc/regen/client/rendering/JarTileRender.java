package me.suff.mc.regen.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.suff.mc.regen.client.animation.AnimationHandler;
import me.suff.mc.regen.client.rendering.model.AlexArmModel;
import me.suff.mc.regen.client.rendering.model.SteveArmModel;
import me.suff.mc.regen.client.skin.SkinHandler;
import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.tiles.JarTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashMap;

/* Created by Craig on 05/03/2021 */
public class JarTileRender extends TileEntityRenderer< JarTile > {

    SteveArmModel steveArmModel = new SteveArmModel();
    AlexArmModel alexArmModel = new AlexArmModel();
    EntityModel mainModel = new AlexArmModel();

    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    public static HashMap< JarTile, ResourceLocation > TEXTURES = new HashMap<>();


    public JarTileRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(JarTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 1.5, 0.5D);
        matrixStackIn.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
        matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
        float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
        FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
        float f2 = (float) (-fontrenderer.getStringPropertyWidth(new TranslationTextComponent(String.valueOf(round(tileEntityIn.getLindos(), 2)))) / 2);
        fontrenderer.func_243247_a(new TranslationTextComponent(String.valueOf(round(tileEntityIn.getLindos(), 2))), f2, (float) 1, -1, false, matrix4f, bufferIn, false, 0, combinedLightIn);
        matrixStackIn.pop();

        if (tileEntityIn.getHand().getItem() instanceof HandItem) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5D, -0.6, 0.5D);
            boolean isAlex = HandItem.isAlex(tileEntityIn.getHand());
            mainModel = isAlex ? alexArmModel : steveArmModel;
            mainModel.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityTranslucent(getOrCreateTexture(tileEntityIn))), combinedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            matrixStackIn.pop();
        } else {
            TEXTURES.remove(tileEntityIn);
        }

    }

    public ResourceLocation getOrCreateTexture(JarTile tileEntityHandInJar) {

        if (!tileEntityHandInJar.getHand().getOrCreateTag().contains("skin")) {
            boolean isAlex = HandItem.isAlex(tileEntityHandInJar.getHand());
            return isAlex ? TEXTURE_ALEX : TEXTURE_STEVE;
        }

        if (!TEXTURES.containsKey(tileEntityHandInJar)) {
            NativeImage image = SkinHandler.genSkinNative(HandItem.getSkin(tileEntityHandInJar.getHand()));
            ResourceLocation res = Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("hand_", new DynamicTexture(image));
            TEXTURES.put(tileEntityHandInJar, res);
            return res;
        }
        return TEXTURES.get(tileEntityHandInJar);
    }


    public static double round(float value, int scale) {
        return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
    }
}
