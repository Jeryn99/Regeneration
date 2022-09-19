package mc.craig.software.regen.common.item.tooltip.fob;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class FobTooltip implements TooltipComponent {

    private final int regenerations;

    public FobTooltip(int regenerations) {
        this.regenerations = regenerations;
    }

    public int getRegenerations() {
        return regenerations;
    }
}
