package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.traits.DnaHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;
import java.io.IOException;

public class GuiRegenerationMenu extends GuiRegenerationBase {
	
	@Override
	public void initGui() {
		super.initGui();
		
		//Buttons
		int cx = (width - xSize) / 2;
		int cy = (height - ySize) / 2;
		final int btnW = 60, btnH = 18;
		
		GuiButtonExt btnColors = new GuiButtonExt(66, cx + 25, cy + 125, btnW, btnH, new TextComponentTranslation("regeneration.gui.previous").getFormattedText());
		GuiButtonExt btnSkinType = new GuiButtonExt(67, cx + 90, cy + 125, btnW, btnH, new TextComponentTranslation("regeneration.gui.next").getFormattedText());
		
		buttonList.add(btnColors);
		buttonList.add(btnSkinType);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		if (button.id == 66) {
			//	Minecraft.getMinecraft().displayGuiScreen(new GuiColorChooser());
		}
		
		if (button.id == 67) {
			//		Minecraft.getMinecraft().displayGuiScreen(new GuiModelChoice());
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		//	Minecraft.getMinecraft().getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int cx = (width - xSize) / 2;
		int cy = (height - ySize) / 2;
		
		String str = " ";
		int length = mc.fontRenderer.getStringWidth(str);
		
		if (RegenConfig.infiniteRegeneration)
			str = new TextComponentTranslation("regeneration.gui.infinite_regenerations").getFormattedText(); // TODO this should be optimized
		else
			str = new TextComponentTranslation("regeneration.gui.remaining_regens.status").getFormattedText() + " " + CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getRegenerationsLeft();
		
		length = mc.fontRenderer.getStringWidth(str);
		fontRenderer.drawString(str, cx + 86 - length / 2, cy + 21, Color.DARK_GRAY.getRGB());
		
		TextComponentTranslation traitLang = new TextComponentTranslation(DnaHandler.getDnaEntry(CapabilityRegeneration.getForPlayer(mc.player).getDnaType()).getLangKey());
		fontRenderer.drawString(traitLang.getUnformattedText(), cx + 86 - length / 2, cy + 30, Color.DARK_GRAY.getRGB());
	}
}
