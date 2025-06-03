package mc.craig.software.regen.common.objects;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.item.*;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

import static net.minecraft.world.item.ArmorMaterials.LEATHER;

public class RItems {


    public static final DeferredRegistry<Item> ITEMS = DeferredRegistry.create(Regeneration.MOD_ID, Registries.ITEM);

    public static RegistrySupplier<Item> FOB = ITEMS.register("fobwatch", FobWatchItem::new);
    public static RegistrySupplier<Item> ZINC = ITEMS.register("zinc", () -> new Item(new Item.Properties()));

}
