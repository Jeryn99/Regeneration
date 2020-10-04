package me.swirtzly.regen.client.gui;

import net.minecraft.entity.player.PlayerEntity;

public class BlankContainer extends net.minecraft.inventory.container.Container {

    public BlankContainer() {
        super(null, 999);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
