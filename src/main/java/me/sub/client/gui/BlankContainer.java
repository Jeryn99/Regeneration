package me.sub.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class BlankContainer extends Container {
    /**
     * Determines whether supplied player can use this container
     *
     * @param playerIn
     */
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
