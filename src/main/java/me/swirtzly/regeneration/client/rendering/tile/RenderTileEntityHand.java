package me.swirtzly.regeneration.client.rendering.tile;

import me.swirtzly.regeneration.client.models.ModelHand;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.common.item.ItemHand;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import static me.swirtzly.regeneration.client.rendering.types.TypeFieryRenderer.renderOverlay;

public class RenderTileEntityHand extends TileEntitySpecialRenderer<TileEntityHandInJar> {

    /**
     * The default skin for the Steve model.
     */
    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
    /**
     * The default skin for the Alex model.
     */
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    public static HashMap<TileEntityHandInJar, ResourceLocation> TEXTURES = new HashMap<>();
    public static ModelBase STEVE_ARM = new ModelHand(false);
    public static ModelBase ALEX_ARM = new ModelHand(true);

    @Override
    public void render(TileEntityHandInJar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        if (te.getHand().getItem() == RegenObjects.Items.HAND) {
            String handType = ItemHand.getSkinType(te.getHand());
            Minecraft.getMinecraft().getTextureManager().bindTexture(getOrCreateTexture(te));
            GlStateManager.rotate(180, 1, 0, 0);
            GlStateManager.translate(0.5, -1.5, -0.5);
            if (handType.equals("ALEX")) {
                ALEX_ARM.render(null, 0, 0, 0, 0, 0, 0.0625F);
                if (te.getLindosAmont() >= 100) {
                    renderOverlay(Minecraft.getMinecraft().player, 0, 0, 0, 0, 0, 0, 0.0625F, ALEX_ARM);
                }
            } else {
                STEVE_ARM.render(null, 0, 0, 0, 0, 0, 0.0625F);
                if (te.getLindosAmont() >= 100) {
                    renderOverlay(Minecraft.getMinecraft().player, 0, 0, 0, 0, 0, 0, 0.0625F, STEVE_ARM);
                }
            }
        } else {
            TEXTURES.remove(te);
        }

        GlStateManager.popMatrix();
    }

    public ResourceLocation getOrCreateTexture(TileEntityHandInJar tileEntityHandInJar) {
        String skinString = ItemHand.getTextureString(tileEntityHandInJar.getHand());
        if (skinString.equalsIgnoreCase("NONE") || skinString.equals(" ") || skinString.equals("")) {
            boolean isAlex = ItemHand.getSkinType(tileEntityHandInJar.getHand()).equalsIgnoreCase("ALEX");
            return isAlex ? TEXTURE_ALEX : TEXTURE_STEVE;
        }

        if (!TEXTURES.containsKey(tileEntityHandInJar)) {
            try {
                BufferedImage image = SkinChangingHandler.toImage(ItemHand.getTextureString(tileEntityHandInJar.getHand()));
                ResourceLocation res = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("hand_", new DynamicTexture(image));
                TEXTURES.put(tileEntityHandInJar, res);
                return res;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return TEXTURES.get(tileEntityHandInJar);
    }


}
