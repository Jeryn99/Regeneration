package mc.craig.software.regen.common.objects;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.item.FobWatchItem;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RTabs {
    public static final DeferredRegistry<CreativeModeTab> TABS = DeferredRegistry.create(Regeneration.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final RegistrySupplier<CreativeModeTab> REGENERATION = TABS.register(Regeneration.MOD_ID, RTabs::createTab);

    public static ItemStack makeIcon() {
        ItemStack stack = new ItemStack(RItems.FOB.get());
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("IsCreativeTab", true);
        stack.setTag(tag);
        return stack;
    }

    public static void outputAccept(CreativeModeTab.Output output) {
        RItems.ITEMS.getEntries().forEach(item -> {
            if (item == RItems.FOB) {
                ItemStack fobGold = new ItemStack(RItems.FOB.get());
                ItemStack fobSilver = new ItemStack(RItems.FOB.get());
                FobWatchItem.setEngrave(fobGold, true);
                FobWatchItem.setEngrave(fobSilver, false);
                output.accept(fobGold);
                output.accept(fobSilver);
            } else {
                output.accept(item.get());
            }
        });
    }

    @ExpectPlatform
    public static CreativeModeTab createTab() {
        throw new RuntimeException("fuck off");
    }
}
