package me.swirtzly.regeneration.client.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiSlider;

import javax.annotation.Nullable;

public class GuiColorSlider extends GuiSlider {

	public GuiColorSlider(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, IPressable handler) {
		super(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, handler);
	}

	public GuiColorSlider(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, IPressable handler, @Nullable ISlider par) {
		super(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, handler, par);
	}

	public GuiColorSlider(int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, IPressable handler, ISlider par) {
		super(xPos, yPos, displayStr, minVal, maxVal, currentVal, handler, par);
	}

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) Math.round(tmp / factor);
	}

	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		sliderValue = round(sliderValue, 2);
		setMessage(dispString + ": " + sliderValue);
		return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
	}

}
