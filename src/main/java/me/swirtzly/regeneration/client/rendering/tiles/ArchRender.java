package me.swirtzly.regeneration.client.rendering.tiles;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.common.tiles.ArchTile;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Swirtzly
 * on 22/04/2020 @ 17:46
 */
public class ArchRender extends TileEntityRenderer<ArchTile> {


    @Override
    public void render(ArchTile tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {

        GlStateManager.pushMatrix();
        GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);

        GlStateManager.pushMatrix();
        RenderUtil.setupRenderLightning();
        RenderUtil.drawGlowingLine(new Vec3d(0.0D, 10D, 0D), new Vec3d(x + 0.5, y + 0.5, z + 0.5), 0.5F, new Vec3d(1, 1, 1), 1);
        RenderUtil.finishRenderLightning();
        GlStateManager.popMatrix();

        Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(RegenObjects.Blocks.ARCH), ItemCameraTransforms.TransformType.GUI);
        GlStateManager.popMatrix();

    }


}
