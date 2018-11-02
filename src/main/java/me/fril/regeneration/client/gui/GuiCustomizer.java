package me.fril.regeneration.client.gui;

import java.awt.Color;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.network.MessageRegenerationStyle;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.RenderUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiCustomizer extends GuiContainer implements GuiSlider.ISlider {
	
	public static ResourceLocation DEFAULT_TEX = new ResourceLocation(RegenerationMod.MODID, "textures/gui/longbg.png");
	public boolean textured = false;
	private float primaryRed, primaryGreen, primaryBlue, secondaryRed, secondaryGreen, secondaryBlue;
	
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
		
		primaryRed = primary.getRed();
		primaryGreen = primary.getGreen();
		primaryBlue = primary.getBlue();
		
		secondaryRed = secondary.getRed();
		secondaryGreen = secondary.getGreen();
		secondaryBlue = secondary.getBlue();
		
		buttonList.add(new GuiButtonExt(0, i + 4, j + 167, 50, 18, new TextComponentTranslation("regeneration.info.save").getFormattedText()));
		buttonList.add(new GuiButtonExt(3, i + 100, j + 167, 50, 18, new TextComponentTranslation("regeneration.info.reset").getFormattedText()));
		buttonList.add(new GuiButtonExt(1, i + 202, j + 167, 50, 18, new TextComponentTranslation("gui.cancel").getFormattedText()));
		
		buttonList.add(new GuiColorSlider(6, i + 20, j + 90, 80, 20, new TextComponentTranslation("regeneration.info.red").getFormattedText(), "", 0, 1, primaryRed, true, true, this));
		buttonList.add(new GuiColorSlider(7, i + 20, j + 110, 80, 20, new TextComponentTranslation("regeneration.info.green").getFormattedText(), "", 0, 1, primaryGreen, true, true, this));
		buttonList.add(new GuiColorSlider(8, i + 20, j + 130, 80, 20, new TextComponentTranslation("regeneration.info.blue").getFormattedText(), "", 0, 1, primaryBlue, true, true, this));
		
		buttonList.add(new GuiColorSlider(9, i + 135, j + 90, 80, 20, new TextComponentTranslation("regeneration.info.red").getFormattedText(), "", 0, 1, secondaryRed, true, true, this));
		buttonList.add(new GuiColorSlider(10, i + 135, j + 110, 80, 20, new TextComponentTranslation("regeneration.info.green").getFormattedText(), "", 0, 1, secondaryGreen, true, true, this));
		buttonList.add(new GuiColorSlider(11, i + 135, j + 130, 80, 20, new TextComponentTranslation("regeneration.info.blue").getFormattedText(), "", 0, 1, secondaryBlue, true, true, this));
	}
	
	public NBTTagCompound getCurrentStyleNBTTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("PrimaryRed", primaryRed);
		nbt.setFloat("PrimaryGreen", primaryGreen);
		nbt.setFloat("PrimaryBlue", primaryBlue);
		nbt.setFloat("SecondaryRed", secondaryRed);
		nbt.setFloat("SecondaryGreen", secondaryGreen);
		nbt.setFloat("SecondaryBlue", secondaryBlue);
		nbt.setBoolean("textured", textured);
		return nbt;
	}
	
	public NBTTagCompound getDefaultStyleNBTTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("PrimaryRed", 0.93f);
		nbt.setFloat("PrimaryGreen", 0.61f);
		nbt.setFloat("PrimaryBlue", 0.0f);
		nbt.setFloat("SecondaryRed", 0.58f);
		nbt.setFloat("SecondaryGreen", 0.29f);
		nbt.setFloat("SecondaryBlue", 0.18f);
		nbt.setBoolean("textured", false);
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
		
		// this.texturedButton.displayString = (textured) ? "Yes" : "No";
		
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
		
		drawString(mc.fontRenderer, new TextComponentTranslation("regeneration.info.customizer").getFormattedText(), i + 5, j + 5, 0xffffff);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) { //TODO change closeScreen to switching back to inventory tab
			case 0: //save
				sendStyleNBTTagToServer(false);
				mc.player.closeScreen();
				break;
			case 1: //cancel
				mc.player.closeScreen();
				break;
			case 3: //reset
				sendStyleNBTTagToServer(true); //TODO change to setting the current values to default
				mc.player.closeScreen();
				break;
			default: throw new RuntimeException("Unknown button id: "+button.id);
		}
	}
	
	private void sendStyleNBTTagToServer(boolean notReset) {
		if (notReset) {
			NetworkHandler.INSTANCE.sendToServer(new MessageRegenerationStyle(getCurrentStyleNBTTag()));
		} else {
			NetworkHandler.INSTANCE.sendToServer(new MessageRegenerationStyle(getDefaultStyleNBTTag()));
		}
		
		NBTTagCompound old = CapabilityRegeneration.getForPlayer(mc.player).getStyle();
		primaryRed = old.getFloat("PrimaryRed");
		primaryGreen = old.getFloat("PrimaryGreen");
		primaryBlue = old.getFloat("PrimaryBlue");
		secondaryRed = old.getFloat("SecondaryRed");
		secondaryGreen = old.getFloat("SecondaryGreen");
		secondaryBlue = old.getFloat("SecondaryBlue");
		textured = old.getBoolean("texture");
	}
	
	@Override
	public void onChangeSliderValue(GuiSlider slider) {
		float val = (float) slider.sliderValue;
		switch (slider.id) {
			case 6:
				primaryRed = val;
				break;
			case 7:
				primaryGreen = val;
				break;
			case 8:
				primaryBlue = val;
				break;
			case 9:
				secondaryRed = val;
				break;
			case 10:
				secondaryGreen = val;
				break;
			case 11:
				secondaryBlue = val;
				break;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
}