package com.lcm.regeneration.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class Blankcontainer extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return false;
	}

}
