package mc.craig.software.regen.client.screen.widgets;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public class DescButton extends Button {

    private ArrayList<Component> description = null;

    public DescButton(int x, int y, int width, int height, Component title, OnPress pressedAction) {
        super(x, y, width, height, title, pressedAction);
    }

    public DescButton(int x, int y, int width, int height, Component title, OnPress pressedAction, OnTooltip onTooltip) {
        super(x, y, width, height, title, pressedAction, onTooltip);
    }

    public ArrayList<Component> getDescription() {
        return description;
    }

    public DescButton setDescription(String[] description) {
        ArrayList<Component> reorderingProcessors = new ArrayList<>();
        for (String textComponent : description) {
            reorderingProcessors.add(Component.translatable(textComponent));
        }
        this.description = reorderingProcessors;
        return this;
    }


}
