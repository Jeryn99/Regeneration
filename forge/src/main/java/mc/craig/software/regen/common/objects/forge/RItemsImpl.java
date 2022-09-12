package mc.craig.software.regen.common.objects.forge;

import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RItemsImpl {


    public static CreativeModeTab TAB = new CreativeModeTab(RConstants.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RItems.FOB.get());
        }
    };

    public static CreativeModeTab getCreativeTab() {
        return TAB;
    }

}
