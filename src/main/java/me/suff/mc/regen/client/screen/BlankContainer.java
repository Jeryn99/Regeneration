package me.suff.mc.regen.client.screen;

import net.minecraft.world.entity.player.Player;

public class BlankContainer extends net.minecraft.world.inventory.AbstractContainerMenu {

    public BlankContainer() {
        super(null, 999);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return false;
    }
}
