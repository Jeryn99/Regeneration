package me.fril.regeneration.client.gui;

import java.awt.Color;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.network.MessageRegenerationStyle;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.RenderUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiCustomizer extends GuiContainer implements GuiSlider.ISlider {
	
	public static ResourceLocation DEFAULT_TEX = new ResourceLocation(RegenerationMod.MODID, "textures/gui/longbg.png");
	
	private float primaryRed, primaryGreen, primaryBlue, secondaryRed, secondaryGreen, secondaryBlue;
	
	private GuiButtonExt btnSave, btnReset, btnCancel;
	private GuiColorSlider slidePrimaryRed, slidePrimaryGreen, slidePrimaryBlue, slideSecondaryRed, slideSecondaryGreen, slideSecondaryBlue;
	
	
	public GuiCustomizer() {
		super(new BlankContainer());
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		xSize = 256;
		ySize = 189;
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		
		IRegeneration cap = CapabilityRegeneration.getForPlayer(mc.player);
		Color primary = cap.getPrimaryColor(), secondary = cap.getSecondaryColor();
		
		primaryRed = primary.getRed() / 255F;
		primaryGreen = primary.getGreen() / 255F;
		primaryBlue = primary.getBlue() / 255F;
		
		secondaryRed = secondary.getRed() / 255F;
		secondaryGreen = secondary.getGreen() / 255F;
		secondaryBlue = secondary.getBlue() / 255F;
		
		
		
		btnSave = new GuiButtonExt(0, i + 4, j + 167, 50, 18, new TextComponentTranslation("regeneration.info.save").getFormattedText());
		btnReset = new GuiButtonExt(3, i + 100, j + 167, 50, 18, new TextComponentTranslation("regeneration.info.reset").getFormattedText());
		btnCancel = new GuiButtonExt(1, i + 202, j + 167, 50, 18, new TextComponentTranslation("gui.cancel").getFormattedText());
		
		slidePrimaryRed = new GuiColorSlider(6, i + 20, j + 90, 80, 20, new TextComponentTranslation("regeneration.info.red").getFormattedText(), "", 0, 1, primaryRed, true, true, this);
		slidePrimaryGreen = new GuiColorSlider(7, i + 20, j + 110, 80, 20, new TextComponentTranslation("regeneration.info.green").getFormattedText(), "", 0, 1, primaryGreen, true, true, this);
		slidePrimaryBlue = new GuiColorSlider(8, i + 20, j + 130, 80, 20, new TextComponentTranslation("regeneration.info.blue").getFormattedText(), "", 0, 1, primaryBlue, true, true, this);
		
		slideSecondaryRed = new GuiColorSlider(9, i + 135, j + 90, 80, 20, new TextComponentTranslation("regeneration.info.red").getFormattedText(), "", 0, 1, secondaryRed, true, true, this);
		slideSecondaryGreen = new GuiColorSlider(10, i + 135, j + 110, 80, 20, new TextComponentTranslation("regeneration.info.green").getFormattedText(), "", 0, 1, secondaryGreen, true, true, this);
		slideSecondaryBlue = new GuiColorSlider(11, i + 135, j + 130, 80, 20, new TextComponentTranslation("regeneration.info.blue").getFormattedText(), "", 0, 1, secondaryBlue, true, true, this);
		
		
		
		buttonList.add(btnSave);
		buttonList.add(btnReset);
		buttonList.add(btnCancel);
		
		buttonList.add(slidePrimaryRed);
		buttonList.add(slidePrimaryGreen);
		buttonList.add(slidePrimaryBlue);
		
		buttonList.add(slideSecondaryRed);
		buttonList.add(slideSecondaryGreen);
		buttonList.add(slideSecondaryBlue);
	}
	
	public NBTTagCompound getCurrentStyleNBTTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("PrimaryRed", primaryRed);
		nbt.setFloat("PrimaryGreen", primaryGreen);
		nbt.setFloat("PrimaryBlue", primaryBlue);
		nbt.setFloat("SecondaryRed", secondaryRed);
		nbt.setFloat("SecondaryGreen", secondaryGreen);
		nbt.setFloat("SecondaryBlue", secondaryBlue);
		return nbt;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		
		mc.getTextureManager().bindTexture(DEFAULT_TEX);
		this.drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
		IRegeneration capa = CapabilityRegeneration.getForPlayer(mc.player);
		String name = new TextComponentTranslation("regeneration.messages.remaining_regens.status").getFormattedText() + " " + capa.getLivesLeft();
		int length = mc.fontRenderer.getStringWidth(name);
		drawString(mc.fontRenderer, name, i + xSize / 2 - length / 2, j + 30, 0xffffff);
		
		name = new TextComponentTranslation("regeneration.info.primary").getFormattedText();
		length = mc.fontRenderer.getStringWidth(name);
		drawString(mc.fontRenderer, name, i + 70 - length / 2, j + 75, 0xffffff);
		
		name = new TextComponentTranslation("regeneration.info.secondary").getFormattedText();
		length = mc.fontRenderer.getStringWidth(name);
		drawString(mc.fontRenderer, name, i + 185 - length / 2, j + 75, 0xffffff);
		
		RenderUtil.drawRect(i + 99, j + 90, i + 121, j + 150, 0.1F, 0.1F, 0.1F, 1);
		RenderUtil.drawRect(i + 100, j + 91, i + 120, j + 149, primaryRed, primaryGreen, primaryBlue, 1);
		
		RenderUtil.drawRect(i + 214, j + 90, i + 236, j + 150, 0.1F, 0.1F, 0.1F, 1);
		RenderUtil.drawRect(i + 215, j + 91, i + 235, j + 149, secondaryRed, secondaryGreen, secondaryBlue, 1);
		
		//drawString(mc.fontRenderer, new TextComponentTranslation("regeneration.info.customizer").getFormattedText(), i + 5, j + 5, 0xffffff);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == btnSave.id) {
			sendStyleNBTTagToServer();
			TabRegistry.openInventoryGui(); //NOW mouse is reset to middle of the screen
		} else if (button.id == btnCancel.id) {
			TabRegistry.openInventoryGui();
		} else if (button.id == btnReset.id) {
			primaryRed = 0.93f;
			primaryGreen = 0.61f;
			primaryBlue = 0.0f;
			
			secondaryRed = 1f;
			secondaryGreen = 0.11f;
			secondaryBlue = 0.18f;
			
			slidePrimaryRed.setValue(primaryRed);
			slidePrimaryGreen.setValue(primaryGreen);
			slidePrimaryBlue.setValue(primaryBlue);
			
			slideSecondaryRed.setValue(secondaryRed);
			slideSecondaryGreen.setValue(secondaryGreen);
			slideSecondaryBlue.setValue(secondaryBlue);
		}
	}
	
	//NOW customization is lost on death
	
	private void sendStyleNBTTagToServer() {
		NetworkHandler.INSTANCE.sendToServer(new MessageRegenerationStyle(getCurrentStyleNBTTag()));
		
		//XXX why?
		NBTTagCompound old = CapabilityRegeneration.getForPlayer(mc.player).getStyle();
		primaryRed = old.getFloat("PrimaryRed");
		primaryGreen = old.getFloat("PrimaryGreen");
		primaryBlue = old.getFloat("PrimaryBlue");
		
		secondaryRed = old.getFloat("SecondaryRed");
		secondaryGreen = old.getFloat("SecondaryGreen");
		secondaryBlue = old.getFloat("SecondaryBlue");
	}
	
	@Override
	public void onChangeSliderValue(GuiSlider slider) {
		float val = (float) slider.sliderValue;
		
		if (slider.id == slidePrimaryRed.id) primaryRed = val;
		else if (slider.id == slidePrimaryGreen.id) primaryGreen = val;
		else if (slider.id == slidePrimaryBlue.id) primaryBlue = val;
		
		else if (slider.id == slideSecondaryRed.id) secondaryRed = val;
		else if (slider.id == slideSecondaryGreen.id) secondaryGreen = val;
		else if (slider.id == slideSecondaryBlue.id) secondaryBlue = val;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
}