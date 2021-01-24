package me.swirtzly.regeneration.client.gui.parts;

import net.minecraftforge.fml.client.config.GuiSlider;

import javax.annotation.Nullable;

public class ColorSliderWidget extends GuiSlider {

    public ColorSliderWidget(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, IPressable handler) {
        super(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, handler);
    }

    public ColorSliderWidget(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, IPressable handler, @Nullable ISlider par) {
        super(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, handler, par);
    }

    public ColorSliderWidget(int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, IPressable handler, ISlider par) {
        super(xPos, yPos, displayStr, minVal, maxVal, currentVal, handler, par);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) Math.round(tmp / factor);
    }

    @Override
    protected void narrate() {
        super.narrate();
    }

}
