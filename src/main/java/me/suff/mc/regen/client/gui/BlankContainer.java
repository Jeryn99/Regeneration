package me.suff.mc.regen.client.gui;

import net.minecraft.entity.player.PlayerEntity;

public class BlankContainer extends net.minecraft.inventory.container.Container {

    public BlankContainer() {
        super(null, 999);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return false;
    }
}
