package mc.craig.software.regen.common.objects;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.item.*;
import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import static net.minecraft.world.item.ArmorMaterials.LEATHER;

public class RItems {


    public static final DeferredRegistry<Item> ITEMS = DeferredRegistry.create(Regeneration.MOD_ID, Registries.ITEM);
    public static CreativeModeTab MAIN = getCreativeTab();

    public static RegistrySupplier<Item> FOB = ITEMS.register("fobwatch", FobWatchItem::new);
    public static RegistrySupplier<Item> SPAWN_ITEM = ITEMS.register("timelord", SpawnItem::new);
    public static RegistrySupplier<Item> PISTOL = ITEMS.register("staser", () -> new GunItem(18, 5, 4.0F));
    public static RegistrySupplier<Item> RIFLE = ITEMS.register("rifle", () -> new GunItem(30, 10, 10.0F));
    public static RegistrySupplier<Item> PLASMA_CARTRIDGE = ITEMS.register("plasma_cartridge", () -> new Item(new Item.Properties()));

    public static RegistrySupplier<Item> ZINC = ITEMS.register("zinc", () -> new Item(new Item.Properties()));
    public static RegistrySupplier<Item> HAND = ITEMS.register("hand", () -> new HandItem(new Item.Properties().stacksTo(1)));
    public static RegistrySupplier<Item> GUARD_HELMET = ITEMS.register("guard_helmet", () -> new ClothingItem("guard_armor", RMaterials.TIMELORD, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static RegistrySupplier<Item> GUARD_CHEST = ITEMS.register("guard_chest", () -> new ClothingItem("guard_armor",RMaterials.TIMELORD,  ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static RegistrySupplier<Item> GUARD_LEGS = ITEMS.register("guard_legs", () -> new ClothingItem("guard_armor",RMaterials.TIMELORD,  ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static RegistrySupplier<Item> GUARD_FEET = ITEMS.register("guard_feet", () -> new ClothingItem("guard_armor",RMaterials.TIMELORD,  ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));
    public static RegistrySupplier<Item> F_ROBES_HEAD = ITEMS.register("f_robes_head", () -> new ClothingItem("robes_female",LEATHER,  ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1).durability(280)));
    public static RegistrySupplier<Item> F_ROBES_CHEST = ITEMS.register("f_robes_chest", () -> new ClothingItem("robes_female",LEATHER,  ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1).durability(400)));
    public static RegistrySupplier<Item> F_ROBES_LEGS = ITEMS.register("f_robes_legs", () -> new ClothingItem("robes_female",LEATHER,  ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1).durability(375)));
    public static RegistrySupplier<Item> M_ROBES_HEAD = ITEMS.register("m_robes_head", () -> new ClothingItem("robes_male",LEATHER,  ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1).durability(280)));
    public static RegistrySupplier<Item> M_ROBES_CHEST = ITEMS.register("m_robes_chest", () -> new ClothingItem("robes_male", LEATHER,  ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1).durability(400)));
    public static RegistrySupplier<Item> M_ROBES_LEGS = ITEMS.register("m_robes_legs", () -> new ClothingItem("robes_male",LEATHER,  ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1).durability(375)));
    public static RegistrySupplier<Item> ROBES_FEET = ITEMS.register("robes_feet", () -> new ClothingItem("robes_male",LEATHER, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1).durability(350)));
    public static RegistrySupplier<Item> GAUNTLET = ITEMS.register("chalice", ChaliceItem::new);


    @ExpectPlatform
    public static CreativeModeTab getCreativeTab() {
        throw new AssertionError();
    }

}
