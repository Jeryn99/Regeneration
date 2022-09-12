package mc.craig.software.regen.common.objects.fabric;

import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.util.RConstants;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RItemsImpl {

    public static final CreativeModeTab TAB = FabricItemGroupBuilder.build(
            new ResourceLocation(RConstants.MODID, RConstants.MODID),
            () -> new ItemStack(RItems.FOB.get()));

    public static CreativeModeTab getCreativeTab() {
        return TAB;
    }

}
