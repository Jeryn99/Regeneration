package mc.craig.software.regen.common.objects;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.item.*;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import static net.minecraft.world.item.ArmorMaterials.LEATHER;

public class RItems {


    public static final DeferredRegistry<Item> ITEMS = DeferredRegistry.create(Regeneration.MOD_ID, Registry.ITEM_REGISTRY);
    public static CreativeModeTab MAIN = getCreativeTab();

    public static RegistrySupplier<Item> FOB = ITEMS.register("fobwatch", FobWatchItem::new);
    public static RegistrySupplier<Item> SPAWN_ITEM = ITEMS.register("timelord", SpawnItem::new);

    public static RegistrySupplier<Item> ZINC = ITEMS.register("zinc", () -> new Item(new Item.Properties().tab(RItems.MAIN)));
    public static RegistrySupplier<Item> HAND = ITEMS.register("hand", () -> new HandItem(new Item.Properties().tab(RItems.MAIN).stacksTo(1)));
    public static RegistrySupplier<Item> GUARD_HELMET = ITEMS.register("guard_helmet", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlot.HEAD, new Item.Properties().tab(RItems.MAIN).stacksTo(1)));
    public static RegistrySupplier<Item> GUARD_CHEST = ITEMS.register("guard_chest", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlot.CHEST, new Item.Properties().tab(RItems.MAIN).stacksTo(1)));
    public static RegistrySupplier<Item> GUARD_LEGS = ITEMS.register("guard_legs", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlot.LEGS, new Item.Properties().tab(RItems.MAIN).stacksTo(1)));
    public static RegistrySupplier<Item> GUARD_FEET = ITEMS.register("guard_feet", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlot.FEET, new Item.Properties().tab(RItems.MAIN).stacksTo(1)));
    public static RegistrySupplier<Item> F_ROBES_HEAD = ITEMS.register("f_robes_head", () -> new ClothingItem(LEATHER, EquipmentSlot.HEAD, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(280)));
    public static RegistrySupplier<Item> F_ROBES_CHEST = ITEMS.register("f_robes_chest", () -> new ClothingItem(LEATHER, EquipmentSlot.CHEST, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(400)));
    public static RegistrySupplier<Item> F_ROBES_LEGS = ITEMS.register("f_robes_legs", () -> new ClothingItem(LEATHER, EquipmentSlot.LEGS, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(375)));
    public static RegistrySupplier<Item> M_ROBES_HEAD = ITEMS.register("m_robes_head", () -> new ClothingItem(LEATHER, EquipmentSlot.HEAD, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(280)));
    public static RegistrySupplier<Item> M_ROBES_CHEST = ITEMS.register("m_robes_chest", () -> new ClothingItem(LEATHER, EquipmentSlot.CHEST, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(400)));
    public static RegistrySupplier<Item> M_ROBES_LEGS = ITEMS.register("m_robes_legs", () -> new ClothingItem(LEATHER, EquipmentSlot.LEGS, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(375)));
    public static RegistrySupplier<Item> ROBES_FEET = ITEMS.register("robes_feet", () -> new ClothingItem(LEATHER, EquipmentSlot.FEET, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(350)));
    public static RegistrySupplier<Item> PLASMA_CARTRIDGE = ITEMS.register("plasma_cartridge", () -> new Item(new Item.Properties().tab(RItems.MAIN)));
    public static RegistrySupplier<Item> GAUNTLET = ITEMS.register("chalice", ChaliceItem::new);

    public static RegistrySupplier<Item> PISTOL = ITEMS.register("staser", () -> new GunItem(18, 5, 4.0F));
    public static RegistrySupplier<Item> RIFLE = ITEMS.register("rifle", () -> new GunItem(30, 10, 10.0F));

    @ExpectPlatform
    public static CreativeModeTab getCreativeTab() {
        throw new AssertionError();
    }

}
