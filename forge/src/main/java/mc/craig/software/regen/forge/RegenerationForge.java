package mc.craig.software.regen.forge;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.entities.Timelord;
import mc.craig.software.regen.common.entities.Watcher;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.world.structures.pieces.StructurePieces;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.forge.command.RegenArgumentsForge;
import mc.craig.software.regen.forge.data.*;
import mc.craig.software.regen.util.ClientUtil;
import mc.craig.software.regen.util.RegenDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(Regeneration.MOD_ID)
public class RegenerationForge {
    public RegenerationForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        Regeneration.init();
        RegenArgumentsForge.COMMAND_ARGUMENT_TYPES.register(modBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::onAttributeAssign);
        modBus.addListener(this::onGatherData);
    }


    private void clientSetup(final FMLClientSetupEvent event) {
        ClientUtil.doClientStuff();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(StructurePieces::init);
    }

    public void onAttributeAssign(EntityAttributeCreationEvent event) {
        event.put(REntities.TIMELORD.get(), Timelord.createAttributes().build());
        event.put(REntities.WATCHER.get(), Watcher.createAttributes().build());
    }


    public void onGatherData(GatherDataEvent e) {
        DataGenerator generator = e.getGenerator();
        ExistingFileHelper existingFileHelper = e.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();

        CompletableFuture<HolderLookup.Provider> lookupProvider = e.getLookupProvider();
        generator.addProvider(true, new RegenEnglishLang(packOutput));
        generator.addProvider(e.includeServer(),
                new RegenLootTables(generator.getPackOutput(),
                        BuiltInLootTables.all(), List.of(
                                new LootTableProvider.SubProviderEntry(RegenLootTables.ModBlockLoot::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(RegenLootTables.ModEntityLoot::new, LootContextParamSets.ENTITY))));
        RegenBlockTags blockTags = new RegenBlockTags(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(true, blockTags);
        generator.addProvider(true, new RegenItemTags(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(true, new RegenMobEffectsTags(packOutput, Registries.MOB_EFFECT, lookupProvider, existingFileHelper));
        generator.addProvider(true, new RegenRecipes(packOutput));
        generator.addProvider(true, new RegenAdvancementsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(true, new RegenBiomeTags(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(true, new RegenDamageTags(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(true, new RegenDatapackProvider(packOutput, lookupProvider));
    }

}
