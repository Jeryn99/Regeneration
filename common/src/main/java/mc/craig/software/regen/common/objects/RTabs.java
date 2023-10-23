package mc.craig.software.regen.common.objects;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.item.ChaliceItem;
import mc.craig.software.regen.common.item.FobWatchItem;
import mc.craig.software.regen.common.item.HandItem;
import mc.craig.software.regen.common.item.SpawnItem;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import mc.craig.software.regen.util.PlayerUtil;
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
    public static void outputAccept(CreativeModeTab.Output output){
        RItems.ITEMS.getEntries().forEach(item -> {
            if(item == RItems.FOB) {
                ItemStack fobGold = new ItemStack(RItems.FOB.get());
                ItemStack fobSilver = new ItemStack(RItems.FOB.get());
                FobWatchItem.setEngrave(fobGold, true);
                FobWatchItem.setEngrave(fobSilver, false);
                output.accept(fobGold);
                output.accept(fobSilver);
            } else if(item == RItems.GAUNTLET) {
                for (TraitBase trait : TraitRegistry.TRAITS_REGISTRY.getValues()) {
                    ItemStack stack = new ItemStack(RItems.GAUNTLET.get());
                    ChaliceItem.setTrait(stack, trait);
                    output.accept(stack);
                }
            } else if(item == RItems.HAND) {
                for (PlayerUtil.SkinType skinType : PlayerUtil.SkinType.values()) {
                    if (skinType != PlayerUtil.SkinType.EITHER) {
                        ItemStack itemstack = new ItemStack(RItems.HAND.get());
                        HandItem.setSkinType(skinType, itemstack);
                        HandItem.setTrait(TraitRegistry.HUMAN.get(), itemstack);
                        output.accept(itemstack);
                    }
                }
            } else if(item == RItems.SPAWN_ITEM){
                for (SpawnItem.Timelord timelordType : SpawnItem.Timelord.values()) {
                    ItemStack itemstack = new ItemStack(RItems.SPAWN_ITEM.get());
                    SpawnItem.setType(itemstack, timelordType);
                    output.accept(itemstack);
                }
            } else {
                output.accept(item.get());
            }
        });
    }

    @ExpectPlatform
    public static CreativeModeTab createTab(){
        throw new RuntimeException("fuck off");
    }
}
