package me.fril.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiColorSlider extends GuiSlider {

    public GuiColorSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, ISlider parent) {
        super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, parent);
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
        super.mouseDragged(par1Minecraft, par2, par3);
        this.sliderValue = round(sliderValue, 2);
        updateText();
    }

    public void updateText() {
        this.displayString = this.dispString + ": " + sliderValue;
    }

}
