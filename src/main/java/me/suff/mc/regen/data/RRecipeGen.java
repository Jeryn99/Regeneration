package me.suff.mc.regen.data;

import me.suff.mc.regen.common.objects.RBlocks;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class RRecipeGen extends RecipeProvider {
    public RRecipeGen(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer< IFinishedRecipe > consumer) {
        ShapedRecipeBuilder.shapedRecipe(RItems.FOB.get()).patternLine("QIG").patternLine("SES").patternLine("IGI").key('G', Items.GHAST_TEAR).key('I', Items.IRON_INGOT).key('E', RItems.ZINC.get()).key('S', Items.SPIDER_EYE).key('Q', Items.BLAZE_ROD).setGroup("regen").addCriterion("has_crafting_table", hasItem(Blocks.CRAFTING_TABLE)).build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(RBlocks.ZINC_ORE.get().asItem()), RItems.ZINC.get(), 0.7F, 300).addCriterion("has_any_kontron", hasItem(RBlocks.ZINC_ORE.get().asItem())).build(consumer, new ResourceLocation(RConstants.MODID, "smelt_zinc"));
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(RBlocks.ZINC_ORE.get().asItem()), RItems.ZINC.get(), 0.8F, 150).addCriterion("has_any_kontron", hasItem(RBlocks.ZINC_ORE.get().asItem())).build(consumer, new ResourceLocation(RConstants.MODID, "blast_zinc"));

        ShapedRecipeBuilder.shapedRecipe(RItems.GUARD_HELMET.get()).patternLine("ZIZ").patternLine("Z Z").patternLine("   ").key('I', Items.IRON_INGOT).key('Z', RItems.ZINC.get()).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(RItems.GUARD_CHEST.get()).patternLine("Z Z").patternLine("ZIZ").patternLine("ZZZ").key('I', Items.IRON_INGOT).key('Z', RItems.ZINC.get()).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(RItems.GUARD_LEGS.get()).patternLine("Z Z").patternLine("ZIZ").patternLine("ZZZ").key('I', Items.IRON_INGOT).key('Z', RItems.ZINC.get()).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(RItems.GUARD_FEET.get()).patternLine("ZIZ").patternLine("Z Z").patternLine("Z Z").key('I', Items.IRON_INGOT).key('Z', RItems.ZINC.get()).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);


        ShapedRecipeBuilder.shapedRecipe(RItems.F_ROBES_HEAD.get()).patternLine("ZIZ").patternLine("ZLZ").patternLine("   ").key('L', ItemTags.FLOWERS).key('I', RItems.ZINC.get()).key('Z', ItemTags.WOOL).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(RItems.F_ROBES_CHEST.get()).patternLine("Z Z").patternLine("ZLZ").patternLine("ZIZ").key('L', ItemTags.FLOWERS).key('I', RItems.ZINC.get()).key('Z', ItemTags.WOOL).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(RItems.F_ROBES_LEGS.get()).patternLine("Z Z").patternLine("ZLZ").patternLine("ZIZ").key('L', ItemTags.FLOWERS).key('I', RItems.ZINC.get()).key('Z', ItemTags.WOOL).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);

        ShapedRecipeBuilder.shapedRecipe(RItems.M_ROBES_HEAD.get()).patternLine("ZIZ").patternLine("ZLZ").patternLine("   ").key('L', Items.LEATHER).key('I', RItems.ZINC.get()).key('Z', ItemTags.WOOL).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(RItems.M_ROBES_CHEST.get()).patternLine("Z Z").patternLine("ZLZ").patternLine("ZIZ").key('L', Items.LEATHER).key('I', RItems.ZINC.get()).key('Z', ItemTags.WOOL).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(RItems.M_ROBES_LEGS.get()).patternLine("Z Z").patternLine("ZLZ").patternLine("ZIZ").key('L', Items.LEATHER).key('I', RItems.ZINC.get()).key('Z', ItemTags.WOOL).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(RItems.ROBES_FEET.get()).patternLine("Z Z").patternLine("Z Z").patternLine("I I").key('I', RItems.ZINC.get()).key('Z', ItemTags.WOOL).setGroup("regen").addCriterion("has_zinc", hasItem(RItems.ZINC.get())).build(consumer);


    }
}
