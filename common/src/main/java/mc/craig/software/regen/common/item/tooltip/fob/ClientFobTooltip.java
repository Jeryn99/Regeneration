package mc.craig.software.regen.common.item.tooltip.fob;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import mc.craig.software.regen.client.screen.overlay.RegenerationOverlay;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;

public class ClientFobTooltip implements ClientTooltipComponent {

    private final int regenerations;

    public ClientFobTooltip(int regenerations) {
        this.regenerations = regenerations;
    }

    public int getRegenerations() {
        return regenerations;
    }

    @Override
    public int getHeight() {
        return 11;
    }

    @Override
    public int getWidth(Font font) {
        return 120;
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        ClientTooltipComponent.super.renderText(font, x, y, matrix4f, bufferSource);
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        for (int i = 0; i < 12 - regenerations; i++) {
            RenderSystem.setShaderTexture(0, RegenerationOverlay.CUSTOM_ICONS);
            GuiComponent.blit(poseStack, mouseX + (i * 10), mouseY, 34, 0, 9, 9, 256, 256);
            GuiComponent.blit(poseStack, mouseX + (i * 10), mouseY, 52, 0, 9, 9, 256, 256);
        }
    }
}
