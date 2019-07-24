package me.swirtzly.regeneration.client.gui;

import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiColorSlider extends GuiSlider {
	
	public GuiColorSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, ISlider parent) {
		super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, parent);
	}
	
	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
	
	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		sliderValue = round(sliderValue, 2);
		displayString = dispString + ": " + sliderValue;
		return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
	}
	
}
