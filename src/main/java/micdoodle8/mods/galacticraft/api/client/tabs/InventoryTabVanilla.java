package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class InventoryTabVanilla extends AbstractTab {
    public InventoryTabVanilla() {
        super(0, 0, 0, new ItemStack(Blocks.CRAFTING_TABLE));
    }

    @Override
    public void onTabClicked() {
        TabRegistry.openInventoryGui();
    }

    @Override
    public boolean shouldAddToList() {
        return true;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {

    }
}