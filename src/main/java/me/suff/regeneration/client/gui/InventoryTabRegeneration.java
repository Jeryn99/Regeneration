package me.suff.regeneration.client.gui;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.handlers.RegenObjects;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class InventoryTabRegeneration extends AbstractTab {
	
	public InventoryTabRegeneration() {
		super(0, 0, 0, new ItemStack(RegenObjects.Items.FOB_WATCH));
		displayString = "Regeneration";
	}
	
	@Override
	public void onTabClicked() {
		Minecraft.getMinecraft().player.openGui(RegenerationMod.INSTANCE, GuiCustomizer.ID, Minecraft.getMinecraft().world, 0, 0, 0);
	}
	
	@Override
	public boolean shouldAddToList() {
		return true;
	}
	
}
