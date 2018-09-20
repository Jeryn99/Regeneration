package me.sub.client.gui;

import me.sub.common.init.RObjects;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class TabRegeneration extends AbstractTab {

    public TabRegeneration() {
        super(150, 0, 0, new ItemStack(RObjects.Items.FOB_WATCH));
    }

    @Override
    public void onTabClicked() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiCustomizer());
    }

    @Override
    public boolean shouldAddToList() {
        return true;
    }
}
