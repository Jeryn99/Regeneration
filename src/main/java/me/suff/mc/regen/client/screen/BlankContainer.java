package me.suff.mc.regen.client.screen;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class BlankContainer extends AbstractContainerMenu {

    public BlankContainer() {
        super(null, 999);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return false;
    }
}
