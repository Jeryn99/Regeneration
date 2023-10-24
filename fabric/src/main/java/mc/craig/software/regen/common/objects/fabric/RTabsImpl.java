package mc.craig.software.regen.common.objects.fabric;

import mc.craig.software.regen.common.objects.RTabs;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class RTabsImpl {

    public static CreativeModeTab createTab() {
        return FabricItemGroup.builder()
                .icon(RTabs::makeIcon)
                .title(Component.translatable("itemGroup.regen"))
                .displayItems((itemDisplayParameters, output) -> RTabs.outputAccept(output))
                .build();
    }

}
