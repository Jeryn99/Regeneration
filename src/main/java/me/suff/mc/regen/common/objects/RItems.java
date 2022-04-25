package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.common.item.*;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.item.ArmorMaterial.LEATHER;

public class RItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RConstants.MODID);
    public static RegistryObject<Item> PISTOL = ITEMS.register("staser", () -> new GunItem(100, 5, 4.0F));    public static RegistryObject<Item> FOB = ITEMS.register("fobwatch", FobWatchItem::new);
    public static RegistryObject<Item> RIFLE = ITEMS.register("rifle", () -> new GunItem(250, 10, 10.0F));    //Item group
    public static ItemGroup MAIN = new ItemGroup("regen") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RItems.FOB.get());
        }
    };
    public static RegistryObject<Item> SPAWN_ITEM = ITEMS.register("timelord", SpawnItem::new);


    public static RegistryObject<Item> ELIXIR = ITEMS.register("elixir", ElixirItem::new);
    public static RegistryObject<Item> ZINC = ITEMS.register("zinc", () -> new Item(new Item.Properties().tab(RItems.MAIN)));
    public static RegistryObject<Item> HAND = ITEMS.register("hand", () -> new HandItem(new Item.Properties().tab(RItems.MAIN).stacksTo(1)));

    public static Item.Properties clothing = new Item.Properties().tab(RItems.MAIN).stacksTo(1);

    public static RegistryObject<Item> GUARD_HELMET = ITEMS.register("guard_helmet", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlotType.HEAD, clothing));
    public static RegistryObject<Item> GUARD_CHEST = ITEMS.register("guard_chest", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlotType.CHEST, clothing));
    public static RegistryObject<Item> GUARD_LEGS = ITEMS.register("guard_legs", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlotType.LEGS, clothing));
    public static RegistryObject<Item> GUARD_FEET = ITEMS.register("guard_feet", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlotType.FEET, clothing));

    public static RegistryObject<Item> F_ROBES_HEAD = ITEMS.register("f_robes_head", () -> new ClothingItem(LEATHER, EquipmentSlotType.HEAD, clothing.durability(280)));
    public static RegistryObject<Item> F_ROBES_CHEST = ITEMS.register("f_robes_chest", () -> new ClothingItem(LEATHER, EquipmentSlotType.CHEST, clothing.durability(400)));
    public static RegistryObject<Item> F_ROBES_LEGS = ITEMS.register("f_robes_legs", () -> new ClothingItem(LEATHER, EquipmentSlotType.LEGS, clothing.durability(375)));

    public static RegistryObject<Item> M_ROBES_HEAD = ITEMS.register("m_robes_head", () -> new ClothingItem(LEATHER, EquipmentSlotType.HEAD, clothing.durability(280)));
    public static RegistryObject<Item> M_ROBES_CHEST = ITEMS.register("m_robes_chest", () -> new ClothingItem(LEATHER, EquipmentSlotType.CHEST, clothing.durability(400)));
    public static RegistryObject<Item> M_ROBES_LEGS = ITEMS.register("m_robes_legs", () -> new ClothingItem(LEATHER, EquipmentSlotType.LEGS, clothing.durability(375)));

    public static RegistryObject<Item> ROBES_FEET = ITEMS.register("robes_feet", () -> new ClothingItem(LEATHER, EquipmentSlotType.FEET, clothing.durability(350)));
    public static RegistryObject<Item> PLASMA_CARTRIDGE = ITEMS.register("plasma_cartridge", () -> new Item(new Item.Properties().tab(RItems.MAIN)));


}
