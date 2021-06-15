package me.swirtzly.regeneration.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

/**
 * Created by Swirtzly on 22/08/2019 @ 14:47
 */
public class BioContainerScreen extends ContainerScreen<BioContainerContainer> {

    private ResourceLocation GUI = new ResourceLocation(Regeneration.MODID, "textures/gui/hij.png");

    public BioContainerScreen(BioContainerContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderTooltip(mouseX, mouseY);
    }

    @Override
    protected void renderLabels(int mouseX, int mouseY) {
        this.font.draw(new TranslationTextComponent(RegenObjects.Blocks.HAND_JAR.get().getDescriptionId()).getContents(), 8, 25, Color.BLACK.getRGB());
        this.font.draw("Residual Energy: " + getMenu().getTileEntity().getLindosAmont(), 8, 59, Color.BLACK.getRGB());
    }

    @Override
    protected void renderBg(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - this.imageWidth) / 2 + 2;
        int relY = (this.height - this.imageHeight) / 2 + 19;
        this.blit(relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
