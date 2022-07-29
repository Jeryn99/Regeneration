package micdoodle8.mods.galacticraft.api.client.tabs;

import me.suff.mc.regen.client.screen.PreferencesScreen;
import me.suff.mc.regen.common.objects.RItems;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class RegenPrefTab extends AbstractTab {
    public RegenPrefTab() {
        super(0, new ItemStack(RItems.FOB.get()));
    }

    @Override
    public void onTabClicked() {
        Minecraft.getInstance().setScreen(new PreferencesScreen());
    }

    @Override
    public boolean shouldAddToList() {
        return true;
    }


}