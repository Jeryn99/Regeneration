package me.suff.regeneration.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class BlankContainer extends Container {
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return false;
	}
	
}
