package mc.craig.software.regen.client.screen;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

/* Created by Craig on 31/01/2021 */
public class RCheckbox extends Checkbox {

    protected final IInteraction onPress;

    public RCheckbox(int x, int y, int width, int height, Component title, boolean checked, IInteraction iPressable) {
        super(x, y, width, height, title, checked);
        this.onPress = iPressable;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.onPress.onPress(this);
    }

    public interface IInteraction<T extends AbstractWidget> {
        void onPress(T p_onPress_1_);
    }
}
