package mc.craig.software.regen.common.objects.forge;

import mc.craig.software.regen.common.item.FobWatchItem;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.objects.RTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RTabsImpl {
    public static CreativeModeTab createTab(){
        return CreativeModeTab.builder().title(Component.translatable("itemGroup.regeneration"))
                .icon(RTabs::makeIcon).displayItems((itemDisplayParameters, output) ->
                        RTabs.outputAccept(output)).build();
    }

}
