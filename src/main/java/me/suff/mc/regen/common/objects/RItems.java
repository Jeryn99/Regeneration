package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.common.item.*;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.item.ArmorMaterials.LEATHER;

public class RItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RConstants.MODID);
    public static RegistryObject<Item> PISTOL = ITEMS.register("staser", () -> new GunItem(18, 5, 4.0F));    //Item group
    public static RegistryObject<Item> RIFLE = ITEMS.register("rifle", () -> new GunItem(30, 10, 10.0F));    public static CreativeModeTab MAIN = new CreativeModeTab("regen") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(RItems.FOB.get());
        }
    };

    public static RegistryObject<Item> FOB = ITEMS.register("fobwatch", FobWatchItem::new);
    public static RegistryObject<Item> SPAWN_ITEM = ITEMS.register("timelord", SpawnItem::new);


    public static RegistryObject<Item> ELIXIR = ITEMS.register("elixir", ElixirItem::new);
    public static RegistryObject<Item> ZINC = ITEMS.register("zinc", () -> new Item(new Item.Properties().tab(RItems.MAIN)));
    public static RegistryObject<Item> HAND = ITEMS.register("hand", () -> new HandItem(new Item.Properties().tab(RItems.MAIN).stacksTo(1)));
    public static RegistryObject<Item> GUARD_HELMET = ITEMS.register("guard_helmet", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlot.HEAD, new Item.Properties().tab(RItems.MAIN).stacksTo(1)));
    public static RegistryObject<Item> GUARD_CHEST = ITEMS.register("guard_chest", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlot.CHEST, new Item.Properties().tab(RItems.MAIN).stacksTo(1)));
    public static RegistryObject<Item> GUARD_LEGS = ITEMS.register("guard_legs", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlot.LEGS, new Item.Properties().tab(RItems.MAIN).stacksTo(1)));
    public static RegistryObject<Item> GUARD_FEET = ITEMS.register("guard_feet", () -> new ClothingItem(RMaterials.TIMELORD, EquipmentSlot.FEET, new Item.Properties().tab(RItems.MAIN).stacksTo(1)));
    public static RegistryObject<Item> F_ROBES_HEAD = ITEMS.register("f_robes_head", () -> new ClothingItem(LEATHER, EquipmentSlot.HEAD, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(280)));
    public static RegistryObject<Item> F_ROBES_CHEST = ITEMS.register("f_robes_chest", () -> new ClothingItem(LEATHER, EquipmentSlot.CHEST, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(400)));
    public static RegistryObject<Item> F_ROBES_LEGS = ITEMS.register("f_robes_legs", () -> new ClothingItem(LEATHER, EquipmentSlot.LEGS, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(375)));
    public static RegistryObject<Item> M_ROBES_HEAD = ITEMS.register("m_robes_head", () -> new ClothingItem(LEATHER, EquipmentSlot.HEAD, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(280)));
    public static RegistryObject<Item> M_ROBES_CHEST = ITEMS.register("m_robes_chest", () -> new ClothingItem(LEATHER, EquipmentSlot.CHEST, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(400)));
    public static RegistryObject<Item> M_ROBES_LEGS = ITEMS.register("m_robes_legs", () -> new ClothingItem(LEATHER, EquipmentSlot.LEGS, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(375)));
    public static RegistryObject<Item> ROBES_FEET = ITEMS.register("robes_feet", () -> new ClothingItem(LEATHER, EquipmentSlot.FEET, new Item.Properties().tab(RItems.MAIN).stacksTo(1).durability(350)));
    public static RegistryObject<Item> PLASMA_CARTRIDGE = ITEMS.register("plasma_cartridge", () -> new Item(new Item.Properties().tab(RItems.MAIN)));


}
