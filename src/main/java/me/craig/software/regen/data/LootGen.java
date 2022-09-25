package me.craig.software.regen.data;

import me.craig.software.regen.common.objects.RGlobalLoot;
import me.craig.software.regen.util.RConstants;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
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
                LootTables.ABANDONED_MINESHAFT,
                LootTables.BASTION_BRIDGE,
                LootTables.LIBRARIAN_GIFT,
                LootTables.WOODLAND_MANSION,
                LootTables.STRONGHOLD_LIBRARY,
                LootTables.SHIPWRECK_SUPPLY
        };

        for (ResourceLocation currentTable : fobWatchTables) {
            add(currentTable.getPath(), RGlobalLoot.REGEN_LOOT.get(), new RGlobalLoot.RegenerationLoot(
                    new ILootCondition[]{LootTableIdCondition.builder(currentTable).build()}, 15)
            );
        }

        for (ResourceLocation resourceLocation : LootTables.all()) {
            //Fob Treasure
            if (resourceLocation.getPath().contains("treasure")) {
                add(resourceLocation.getPath(), RGlobalLoot.REGEN_LOOT.get(), new RGlobalLoot.RegenerationLoot(
                        new ILootCondition[]{LootTableIdCondition.builder(resourceLocation).build()}, 40)
                );
            }
        }


    }
}
