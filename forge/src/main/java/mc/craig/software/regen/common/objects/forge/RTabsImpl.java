package mc.craig.software.regen.common.objects.forge;

import mc.craig.software.regen.common.objects.RTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class RTabsImpl {
    public static CreativeModeTab createTab() {
        return CreativeModeTab.builder().title(Component.translatable("itemGroup.regen"))
                .icon(RTabs::makeIcon).displayItems((itemDisplayParameters, output) ->
                        RTabs.outputAccept(output)).build();
    }

}
