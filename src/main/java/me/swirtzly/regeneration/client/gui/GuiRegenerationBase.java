package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.gui.parts.BlankContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.ResourceLocation;

public class GuiRegenerationBase extends ContainerScreen {
	
	private static final ResourceLocation background = new ResourceLocation(RegenerationMod.MODID, "textures/gui/customizer_background.png");
	
	public GuiRegenerationBase() {
		super(new BlankContainer());
		xSize = 176;
		ySize = 186;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft.getInstance().getTextureManager().bindTexture(background);
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		super.render(p_render_1_, p_render_2_, p_render_3_);
		renderBackground();
	}

	@Override
	public void renderBackground() {
		super.renderBackground();
	}

}
