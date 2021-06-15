package me.suff.mc.regen.util.common;

import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.ILootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class LootUtils {
    private static final Field tablePools;
    private static final Field poolEntries;
    private static final Field entryItem;

    static {
        tablePools = ObfuscationReflectionHelper.findField(LootTable.class, "pools");
        tablePools.setAccessible(true);
        poolEntries = ObfuscationReflectionHelper.findField(LootPool.class, "entries");
        poolEntries.setAccessible(true);
        entryItem = ObfuscationReflectionHelper.findField(ItemLootEntry.class, "item");
        entryItem.setAccessible(true);
    }


    public static LootPool createLootPool(String name, RandomValueRange numRolls, RandomValueRange bonusRolls, LootEntry.Builder entryBuilder, ILootCondition.IBuilder conditionBuilder, ILootFunction.IBuilder functionBuilder) {
        LootPool.Builder builder = LootPool.lootPool();
        builder.name(name);
        builder.setRolls(numRolls);
        builder.bonusRolls(bonusRolls.getMin(), bonusRolls.getMax());
        builder.add(entryBuilder);
        builder.when(conditionBuilder);
        builder.apply(functionBuilder);

        return builder.build();
    }

    public static void addItemToTable(LootTable table, LootPool pool) {
        table.addPool(pool);
    }

    public static void addItemToTable(LootTable table, Item item, int weight, float probability, int minQuantity, int maxQuantity, String name) {
        addItemToTable(table, item, weight, 1, probability, minQuantity, maxQuantity, name);
    }

    public static void addItemToTable(LootTable table, Item item, int weight, float probability, int quantity, String name) {
        addItemToTable(table, item, weight, 1, probability, quantity, quantity, name);
    }

    private static void addItemToTable(LootTable table, Item item, int weight, float numRolls, float probability, int minQuantity, int maxQuantity, String name) {
        ILootCondition.IBuilder conditionBuilder = RandomChance.randomChance(probability);
        ILootFunction.IBuilder functionBuilder = SetCount.setCount(new RandomValueRange(minQuantity, maxQuantity));

        StandaloneLootEntry.Builder entryBuilder = ItemLootEntry.lootTableItem(item);
        entryBuilder.setWeight(weight);
        entryBuilder.setQuality(1);
        entryBuilder.when(conditionBuilder);
        entryBuilder.apply(functionBuilder);

        LootPool newPool = createLootPool(name, new RandomValueRange(numRolls), new RandomValueRange(0), entryBuilder, conditionBuilder, functionBuilder);

        addItemToTable(table, newPool);
    }
}
