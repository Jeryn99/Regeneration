package me.swirtzly.regen.client.gui;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.ITextComponent;

/* Created by Craig on 31/01/2021 */
public class RCheckbox extends CheckboxButton {

    protected final ICheck onPress;

    public RCheckbox(int x, int y, int width, int height, ITextComponent title, boolean checked, ICheck iPressable) {
        super(x, y, width, height, title, checked);
        this.onPress = iPressable;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.onPress.onPress(this);
    }

    public interface ICheck {
        void onPress(CheckboxButton p_onPress_1_);
    }
}
