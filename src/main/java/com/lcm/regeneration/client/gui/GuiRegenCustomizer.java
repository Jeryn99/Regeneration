package com.lcm.regeneration.client.gui;

import com.lcm.regeneration.superpower.TimelordSuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.gui.GuiCustomizer;
import lucraft.mods.lucraftcore.util.gui.GuiColorSlider;
import lucraft.mods.lucraftcore.util.helper.LCRenderHelper;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class GuiRegenCustomizer extends GuiCustomizer implements GuiSlider.ISlider {
	
	public float primaryRed;
	public float primaryGreen;
	public float primaryBlue;
	public float secondaryRed;
	public float secondaryGreen;
	public float secondaryBlue;
	
	public TimelordSuperpowerHandler data;
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.xSize = 256;
		this.ySize = 189;
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		
		data = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(mc.player, TimelordSuperpowerHandler.class);
		
		NBTTagCompound old = SuperpowerHandler.getSuperpowerPlayerHandler(mc.player).getStyleNBTTag();
		
		primaryRed = old.getFloat("PrimaryRed");
		primaryGreen = old.getFloat("PrimaryGreen");
		primaryBlue = old.getFloat("PrimaryBlue");
		secondaryRed = old.getFloat("SecondaryRed");
		secondaryGreen = old.getFloat("SecondaryGreen");
		secondaryBlue = old.getFloat("SecondaryBlue");
		
		this.buttonList.add(new GuiButtonExt(0, i + 4, j + 167, 50, 18, StringHelper.translateToLocal("lucraftcore.info.save")));
		this.buttonList.add(new GuiButtonExt(1, i + 202, j + 167, 50, 18, StringHelper.translateToLocal("gui.cancel")));
		
		this.buttonList.add(new GuiColorSlider(6, i + 20, j + 70, 80, 20, StringHelper.translateToLocal("lucraftcore.info.red"), "", 0, 1, primaryRed, true, true, this));
		this.buttonList.add(new GuiColorSlider(7, i + 20, j + 90, 80, 20, StringHelper.translateToLocal("lucraftcore.info.green"), "", 0, 1, primaryGreen, true, true, this));
		this.buttonList.add(new GuiColorSlider(8, i + 20, j + 110, 80, 20, StringHelper.translateToLocal("lucraftcore.info.blue"), "", 0, 1, primaryBlue, true, true, this));
		
		this.buttonList.add(new GuiColorSlider(9, i + 135, j + 70, 80, 20, StringHelper.translateToLocal("lucraftcore.info.red"), "", 0, 1, secondaryRed, true, true, this));
		this.buttonList.add(new GuiColorSlider(10, i + 135, j + 90, 80, 20, StringHelper.translateToLocal("lucraftcore.info.green"), "", 0, 1, secondaryGreen, true, true, this));
		this.buttonList.add(new GuiColorSlider(11, i + 135, j + 110, 80, 20, StringHelper.translateToLocal("lucraftcore.info.blue"), "", 0, 1, secondaryBlue, true, true, this));
		
	}
	
	@Override
	public NBTTagCompound getStyleNBTTag() {
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
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		
		if (SuperpowerHandler.getSuperpower(mc.player).getPlayerRenderer() != null)
			SuperpowerHandler.getSuperpower(mc.player).getPlayerRenderer().applyColor();
		
		mc.getTextureManager().bindTexture(GuiCustomizer.DEFAULT_TEX);
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		
		String name = StringHelper.translateToLocal("lcm-regen.info.primary");
		int length = mc.fontRenderer.getStringWidth(name);
		this.drawString(mc.fontRenderer, name, i + 70 - length / 2, j + 55, 0xffffff);
		
		name = StringHelper.translateToLocal("lcm-regen.info.secondary");
		length = mc.fontRenderer.getStringWidth(name);
		this.drawString(mc.fontRenderer, name, i + 185 - length / 2, j + 55, 0xffffff);
		
		LCRenderHelper.drawRect(i + 99, j + 70, i + 121, j + 130, 0.1F, 0.1F, 0.1F, 1);
		LCRenderHelper.drawRect(i + 100, j + 71, i + 120, j + 129, primaryRed, primaryGreen, primaryBlue, 1);
		
		LCRenderHelper.drawRect(i + 214, j + 70, i + 236, j + 130, 0.1F, 0.1F, 0.1F, 1);
		LCRenderHelper.drawRect(i + 215, j + 71, i + 235, j + 129, secondaryRed, secondaryGreen, secondaryBlue, 1);
		
		this.drawString(mc.fontRenderer, StringHelper.translateToLocal("lucraftcore.info.customizer"), i + 5, j + 5, 0xffffff);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			sendStyleNBTTagToServer();
			mc.player.closeScreen();
		}
		if (button.id == 1) mc.player.closeScreen();
	}
	
	@Override
	public void onChangeSliderValue(GuiSlider slider) {
		if (slider.id == 6)
			this.primaryRed = (float) slider.sliderValue;
		else if (slider.id == 7)
			this.primaryGreen = (float) slider.sliderValue;
		else if (slider.id == 8)
			this.primaryBlue = (float) slider.sliderValue;
		else if (slider.id == 9)
			this.secondaryRed = (float) slider.sliderValue;
		else if (slider.id == 10)
			this.secondaryGreen = (float) slider.sliderValue;
		else if (slider.id == 11) this.secondaryBlue = (float) slider.sliderValue;
	}
	
}