package mc.craig.software.regen.common.item.tooltip.hand;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class HandSkinToolTip implements TooltipComponent {

    private byte[] skin = new byte[0];
    private boolean isAlex = false;

    public HandSkinToolTip(byte[] skin, boolean isAlex) {
        this.skin = skin;
        this.isAlex = isAlex;
    }

    public byte[] getSkin() {
        return skin;
    }

    public boolean getModel() {
        return isAlex;
    }
}
