package mc.craig.software.regen.forge.data;

import com.mojang.datafixers.util.Pair;
import mc.craig.software.regen.common.objects.RBlocks;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.registry.RegistrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegenLootTables extends LootTableProvider {

    public RegenLootTables(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return List.of(Pair.of(ModEntityLoot::new, LootContextParamSets.ENTITY), Pair.of(ModBlockLoot::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
        for (Map.Entry<ResourceLocation, LootTable> entry : map.entrySet())
            LootTables.validate(validationContext, entry.getKey(), entry.getValue());
    }

    public static class ModBlockLoot extends BlockLoot {
        @Override
        protected void addTables() {
            for (Block block : getKnownBlocks()) {
                dropSelf(block);
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            ArrayList<@NotNull Block> blocks = new ArrayList<>();
            for (RegistrySupplier<Block> entry : RBlocks.BLOCKS.getEntries()) {
                blocks.add(entry.get());
            }
            return blocks;
        }
    }

    public static class ModEntityLoot extends EntityLoot {
        @Override
        protected void addTables() {
            // add(knownEntity, LootTable.lootTable());
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            ArrayList<EntityType<?>> entityTypes = new ArrayList<>();
            for (RegistrySupplier<EntityType<?>> entry : REntities.ENTITY_TYPES.getEntries()) {
                entityTypes.add(entry.get());
            }
            return entityTypes;
        }
    }
}