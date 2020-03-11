package me.swirtzly.regeneration.client.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiColorSlider extends GuiSlider {

    public GuiColorSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, ISlider parent) {
        super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, parent);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int x, int y) {
        super.mouseDragged(mc, x, y);
        sliderValue = round(sliderValue, 2);
        displayString = dispString + ": " + sliderValue;
    }
	
}
