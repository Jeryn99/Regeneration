package mc.craig.software.regen.forge.data;

import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import mc.craig.software.regen.common.objects.RBlocks;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.registry.RegistrySupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class RegenLootTables extends LootTableProvider {
    public RegenLootTables(PackOutput output, Set<ResourceLocation> requiredTables, List<SubProviderEntry> subProviders) {
        super(output, requiredTables, subProviders);
    }

    public static class ModBlockLoot extends BlockLootSubProvider {
        public ModBlockLoot(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures) {
            super(explosionResistant, enabledFeatures);
        }
        public ModBlockLoot() {
            super(Collections.emptySet(), FeatureFlags.VANILLA_SET);
        }

        @Override
        protected void generate() {
            this.add(RBlocks.ZINC_ORE.get(), (block) -> createOreDrop(block, RItems.ZINC.get()));
            this.add(RBlocks.ZINC_ORE_DEEPSLATE.get(), (block) -> createOreDrop(block, RItems.ZINC.get()));
            dropSelf(RBlocks.ZERO_ROUNDEL.get());
            dropSelf(RBlocks.BIO_CONTAINER.get());
            dropSelf(RBlocks.AZBANTIUM.get());
            dropSelf(RBlocks.ZERO_ROOM_FULL.get());
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

    public static class ModEntityLoot extends EntityLootSubProvider {
        public ModEntityLoot(FeatureFlagSet enabledFeatures) {
            super(enabledFeatures);
        }

        public ModEntityLoot(){
            super(FeatureFlags.VANILLA_SET);
        }

        @Override
        public void generate() {
            add(REntities.TIMELORD.get(), LootTable.lootTable());
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            Stream.Builder<EntityType<?>> entityTypes = Stream.builder();
            for (RegistrySupplier<EntityType<?>> entry : REntities.ENTITY_TYPES.getEntries()) {
                entityTypes.add(entry.get());
            }
            return entityTypes.build();
        }
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext) {
        map.forEach((arg, arg2) -> {
            arg2.validate(validationcontext.setParams(arg2.getParamSet()).enterElement("{" + arg + "}", new LootDataId(LootDataType.TABLE, arg)));
        });
    }
}