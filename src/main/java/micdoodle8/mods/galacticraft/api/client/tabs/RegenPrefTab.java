package micdoodle8.mods.galacticraft.api.client.tabs;

import me.swirtzly.regeneration.client.gui.GuiPreferences;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.handlers.acting.ActingForwarder;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class RegenPrefTab extends AbstractTab {
	public RegenPrefTab() {
		super(0, 0, 0, new ItemStack(RegenObjects.Items.FOB_WATCH.get()));
	}

	@Override
	public void onTabClicked() {
		Minecraft.getInstance().displayGuiScreen(new GuiPreferences());
	}

	@Override
	public boolean shouldAddToList() {
		return true;
	}
}