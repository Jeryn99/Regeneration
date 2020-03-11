package me.swirtzly.regeneration.client.gui.parts;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

/**
 * Created by Sub on 20/09/2018.
 */
public class InventoryTabRegeneration extends AbstractTab {

    public InventoryTabRegeneration() {
        super(0, 0, 0, new ItemStack(RegenObjects.Items.FOB_WATCH));
        displayString = "Regeneration";
    }

    @Override
    public void onTabClicked() {

    }

    @Override
    public boolean shouldAddToList() {
        return Minecraft.getMinecraft().player != null && CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getRegenerationsLeft() > 0;
    }
	
}
