package mc.craig.software.regen.common.item.tooltip.hand;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.client.screen.IncarnationScreen;
import mc.craig.software.regen.client.skin.VisualManipulator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.Arrays;

public class ClientHandSkinToolTip implements ClientTooltipComponent {


    private static final ResourceLocation STEVE_SKIN_LOCATION = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation ALEX_SKIN_LOCATION = new ResourceLocation("textures/entity/alex.png");
    private byte[] skin = new byte[0];
    private boolean isAlex = false;

    public ClientHandSkinToolTip(byte[] skin, boolean isAlex) {
        this.skin = skin;
        this.isAlex = isAlex;
    }

    public byte[] getSkin() {
        return skin;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public int getWidth(Font font) {
        return 10;
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        ClientTooltipComponent.super.renderText(font, x, y, matrix4f, bufferSource);
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        IncarnationScreen.currentTexture = Arrays.equals(skin, new byte[0]) ? (isAlex ? ALEX_SKIN_LOCATION : STEVE_SKIN_LOCATION) : VisualManipulator.loadImage(VisualManipulator.genSkinNative(getSkin()));
        guiGraphics.blit(IncarnationScreen.currentTexture, mouseX, mouseY, 8, 8, 8, 8, 64, 64);
        guiGraphics.blit(IncarnationScreen.currentTexture, mouseX, mouseY, 40, 8, 8, 8, 64, 64);
        guiGraphics.pose().popPose();
    }
}
