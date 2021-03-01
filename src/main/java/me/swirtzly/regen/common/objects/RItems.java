package me.swirtzly.regen.common.objects;

import me.swirtzly.regen.common.item.ElixirItem;
import me.swirtzly.regen.common.item.FobWatchItem;
import me.swirtzly.regen.common.item.GunItem;
import me.swirtzly.regen.common.item.SpawnItem;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RItems {

    //Item group
    public static ItemGroup MAIN = new ItemGroup("regen") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RItems.FOB.get());
        }
    };

    public static final DeferredRegister< Item > ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RConstants.MODID);
    public static RegistryObject< Item > FOB = ITEMS.register("fobwatch", FobWatchItem::new);
    public static RegistryObject< Item > SPAWN_ITEM = ITEMS.register("timelord", SpawnItem::new);
    public static RegistryObject< Item > PISTOL = ITEMS.register("staser", () -> new GunItem(18, 5, 4.0F));
    public static RegistryObject< Item > RIFLE = ITEMS.register("rifle", () -> new GunItem(30, 10, 10.0F));
    public static RegistryObject< Item > ELIXIR = ITEMS.register("elixir", ElixirItem::new);

}
