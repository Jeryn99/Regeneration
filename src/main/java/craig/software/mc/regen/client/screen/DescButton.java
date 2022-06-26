package craig.software.mc.regen.client.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;

public class DescButton extends Button {

    private ArrayList<FormattedCharSequence> description = null;

    public DescButton(int x, int y, int width, int height, Component title, OnPress pressedAction) {
        super(x, y, width, height, title, pressedAction);
    }

    public DescButton(int x, int y, int width, int height, Component title, OnPress pressedAction, OnTooltip onTooltip) {
        super(x, y, width, height, title, pressedAction, onTooltip);
    }

    public ArrayList<FormattedCharSequence> getDescription() {
        return description;
    }

    public DescButton setDescription(String[] description) {
        ArrayList<FormattedCharSequence> reorderingProcessors = new ArrayList<>();
        for (String textComponent : description) {
            reorderingProcessors.add(Component.translatable(textComponent).getVisualOrderText());
        }
        this.description = reorderingProcessors;
        return this;
    }


}
