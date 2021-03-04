package me.suff.mc.regen.data;

import me.suff.mc.regen.common.item.ClothingItem;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

/* Created by Craig on 04/03/2021 */
public class ItemModelGen extends ItemModelProvider {

    public ItemModelGen(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, RConstants.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof ClothingItem) {
                getBuilder(item.getRegistryName().toString())
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", modLoc("item/" + item.getRegistryName().getPath()));
            }
        }
    }

}
