package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.gui.parts.BlankContainer;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;
import java.io.IOException;

import static me.swirtzly.regeneration.util.ClientUtil.playerModelAlex;
import static me.swirtzly.regeneration.util.ClientUtil.playerModelSteve;
import static me.swirtzly.regeneration.util.RenderUtil.drawModelToGui;

public class GuiModelChoice extends GuiContainer {

	public static final int ID = 1;
	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(RegenerationMod.MODID, "textures/gui/customizer_background.png");
	private static SkinChangingHandler.EnumChoices CHOICES = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getPreferredModel();
    private float ROTATION = 0;
	
	public GuiModelChoice() {
		super(new BlankContainer());
		xSize = 176;
		ySize = 186;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int cx = (width - xSize) / 2;
		int cy = (height - ySize) / 2;
		final int btnW = 60, btnH = 18;
        ROTATION = 0;
		GuiButtonExt btnNext = new GuiButtonExt(1, cx + 25, cy + 125, btnW, btnH, new TextComponentTranslation("regeneration.gui.previous").getFormattedText());
		GuiButtonExt btnPrevious = new GuiButtonExt(4, cx + 90, cy + 125, btnW, btnH, new TextComponentTranslation("regeneration.gui.next").getFormattedText());
		GuiButtonExt btnBack = new GuiButtonExt(98, cx + 25, cy + 145, btnW, btnH, new TextComponentTranslation("regeneration.gui.back").getFormattedText());
		GuiButtonExt btnOpenFolder = new GuiButtonExt(99, cx + 90, cy + 145, btnW, btnH, new TextComponentTranslation("regeneration.gui.skin_choice").getFormattedText());
		
		buttonList.add(btnNext);
		buttonList.add(btnPrevious);
		buttonList.add(btnOpenFolder);
		buttonList.add(btnBack);
		
		CHOICES = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getPreferredModel();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		GlStateManager.pushMatrix();
		playerModelAlex.isChild = false;
		playerModelSteve.isChild = false;
		switch (CHOICES) {
			case ALEX:
				Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_ALEX);
                drawModelToGui(playerModelAlex, width / 2, height / 2 - 40, 1.0f, ROTATION);
				break;
			case STEVE:
				Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_STEVE);
                drawModelToGui(playerModelSteve, width / 2, height / 2 - 40, 1.0f, ROTATION);
				break;
			case EITHER:
				Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_ALEX);
                drawModelToGui(playerModelAlex, width / 2 - 40, height / 2 - 40, 1.0f, ROTATION);
				Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_STEVE);
                drawModelToGui(playerModelSteve, width / 2 + 40, height / 2 - 40, 1.0f, ROTATION);
				break;
		}
		GlStateManager.popMatrix();
		
		drawCenteredString(Minecraft.getMinecraft().fontRenderer, new TextComponentTranslation("regeneration.gui.preference_model", CHOICES.name()).getUnformattedText(), width / 2, height / 2 + 15, Color.WHITE.getRGB());
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		switch (button.id) {
			case 98:
				Minecraft.getMinecraft().player.openGui(RegenerationMod.INSTANCE, GuiCustomizer.ID, Minecraft.getMinecraft().world, 0, 0, 0);
				break;
			
			case 4:
				//Next
				if (CHOICES.previous() != null) {
					CHOICES = (SkinChangingHandler.EnumChoices) CHOICES.previous();
				} else {
					CHOICES = SkinChangingHandler.EnumChoices.EITHER;
				}
				PlayerUtil.updateModel(CHOICES);
				break;
			
			case 1:
				//Previous
				if (CHOICES.next() != null) {
					CHOICES = (SkinChangingHandler.EnumChoices) CHOICES.next();
				} else {
					CHOICES = SkinChangingHandler.EnumChoices.ALEX;
				}
				PlayerUtil.updateModel(CHOICES);
				break;
			
			case 99:
				Minecraft.getMinecraft().player.openGui(RegenerationMod.INSTANCE, GuiSkinChange.ID, Minecraft.getMinecraft().world, 0, 0, 0);
				break;
		}
	}
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
        ROTATION++;
        if (ROTATION > 360) {
            ROTATION = 0;
		}
	}
	
}
