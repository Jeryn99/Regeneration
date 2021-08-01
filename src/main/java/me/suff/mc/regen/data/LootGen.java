package me.suff.mc.regen.data;

import me.suff.mc.regen.common.objects.RGlobalLoot;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

/* Created by Craig on 10/03/2021 */
public class LootGen extends GlobalLootModifierProvider {
    public LootGen(DataGenerator gen) {
        super(gen, RConstants.MODID);
    }

    @Override
    protected void start() {

        //FOB WATCH
        ResourceLocation[] fobWatchTables = new ResourceLocation[]{
                BuiltInLootTables.ABANDONED_MINESHAFT,
                BuiltInLootTables.BASTION_BRIDGE,
                BuiltInLootTables.LIBRARIAN_GIFT,
                BuiltInLootTables.WOODLAND_MANSION,
                BuiltInLootTables.STRONGHOLD_LIBRARY,
                BuiltInLootTables.SHIPWRECK_SUPPLY
        };

        for (ResourceLocation currentTable : fobWatchTables) {
            add(currentTable.getPath(), RGlobalLoot.REGEN_LOOT.get(), new RGlobalLoot.RegenerationLoot(
                    new LootItemCondition[]{LootTableIdCondition.builder(currentTable).build()}, 15)
            );
        }

        for (ResourceLocation resourceLocation : BuiltInLootTables.all()) {
            //Fob Treasure
            if (resourceLocation.getPath().contains("treasure")) {
                add(resourceLocation.getPath(), RGlobalLoot.REGEN_LOOT.get(), new RGlobalLoot.RegenerationLoot(
                        new LootItemCondition[]{LootTableIdCondition.builder(resourceLocation).build()}, 40)
                );
            }
        }


    }
}
