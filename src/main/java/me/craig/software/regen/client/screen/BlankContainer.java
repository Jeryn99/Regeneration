package me.craig.software.regen.client.screen;

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
