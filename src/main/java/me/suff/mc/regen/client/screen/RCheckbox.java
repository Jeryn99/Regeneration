package me.suff.mc.regen.client.screen;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.ITextComponent;

/* Created by Craig on 31/01/2021 */
public class RCheckbox extends CheckboxButton {

    protected final IInteraction onPress;

    public RCheckbox(int x, int y, int width, int height, ITextComponent title, boolean checked, IInteraction iPressable) {
        super(x, y, width, height, title, checked);
        this.onPress = iPressable;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.onPress.onPress(this);
    }

    public interface IInteraction< T extends Widget > {
        void onPress(T p_onPress_1_);
    }
}
