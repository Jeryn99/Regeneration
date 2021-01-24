package me.swirtzly.regen.common.objects;

import me.swirtzly.regen.common.item.ElixirItem;
import me.swirtzly.regen.common.item.FobWatchItem;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RItems {

    public static final DeferredRegister< Item > ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RConstants.MODID);
    //Item group
    public static ItemGroup MAIN = new ItemGroup("regen") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RItems.FOB.get());
        }
    };
    public static RegistryObject< Item > FOB = ITEMS.register("fobwatch", FobWatchItem::new);
    public static RegistryObject< Item > ELIXIR = ITEMS.register("elixir", ElixirItem::new);

}
