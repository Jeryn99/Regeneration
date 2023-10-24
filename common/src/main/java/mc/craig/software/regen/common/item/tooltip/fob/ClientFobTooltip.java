package mc.craig.software.regen.common.item.tooltip.fob;

import com.mojang.blaze3d.systems.RenderSystem;
import mc.craig.software.regen.client.screen.overlay.RegenerationOverlay;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

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
    public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        for (int i = 0; i < 12 - regenerations; i++) {
            RenderSystem.setShaderTexture(0, RegenerationOverlay.CUSTOM_ICONS);
            guiGraphics.blit(RegenerationOverlay.CUSTOM_ICONS, mouseX + (i * 10), mouseY, 34, 0, 9, 9, 256, 256);
            guiGraphics.blit(RegenerationOverlay.CUSTOM_ICONS, mouseX + (i * 10), mouseY, 52, 0, 9, 9, 256, 256);
        }
    }
}
