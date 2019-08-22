package me.swirtzly.regeneration.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

/**
 * Created by Swirtzly
 * on 22/08/2019 @ 14:47
 */
public class BioContainerScreen extends ContainerScreen<BioContainerContainer> {

    private ResourceLocation GUI = new ResourceLocation(RegenerationMod.MODID, "textures/gui/hij.png");

    public BioContainerScreen(BioContainerContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(new TranslationTextComponent(RegenObjects.Blocks.HAND_JAR.getTranslationKey()).getUnformattedComponentText(), 8, 6, Color.BLACK.getRGB());
        this.font.drawString("Residual Energy: " + getContainer().getTileEntity().getLindosAmont(), 8, 40, Color.BLACK.getRGB());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    }
}
