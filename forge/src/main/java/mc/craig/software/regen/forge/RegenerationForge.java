package mc.craig.software.regen.forge;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.entities.Timelord;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.objects.RSoundSchemes;
import mc.craig.software.regen.common.world.structures.pieces.StructurePieces;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.forge.data.*;
import mc.craig.software.regen.util.ClientUtil;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
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

@Mod(Regeneration.MOD_ID)
public class RegenerationForge {
    public RegenerationForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        Regeneration.init();
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
        RSoundSchemes.init();

        event.enqueueWork(StructurePieces::init);
    }

    public void onAttributeAssign(EntityAttributeCreationEvent event) {
        event.put(REntities.TIMELORD.get(), Timelord.createAttributes().build());
    }


    public void onGatherData(GatherDataEvent e) {
        DataGenerator generator = e.getGenerator();
        ExistingFileHelper existingFileHelper = e.getExistingFileHelper();
        generator.addProvider(true, new RegenEnglishLang(generator));
        generator.addProvider(true, new RegenLootTables(generator));
        RegenBlockTags blockTags = new RegenBlockTags(generator, existingFileHelper);
        generator.addProvider(true, blockTags);
        generator.addProvider(true, new RegenItemTags(generator, blockTags, existingFileHelper));
        generator.addProvider(true, new RegenRecipes(generator));
        generator.addProvider(true, new RegenBiomeModifiers(generator));
        generator.addProvider(true, new RegenAdvancements(generator));
        generator.addProvider(true, new RegenBiomeTags(generator, existingFileHelper));
    }

}
