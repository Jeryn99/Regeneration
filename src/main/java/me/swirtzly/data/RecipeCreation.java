package me.swirtzly.data;

import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class RecipeCreation extends RecipeProvider {
    public RecipeCreation(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        // Crafting Table
        ShapedRecipeBuilder.shapedRecipe(RegenObjects.Items.SEAL.get()).patternLine(" G ").patternLine("G G").patternLine(" G ").key('G', RegenObjects.Items.GAL_INGOT.get()).addCriterion("has_crafting_table", this.hasItem(Blocks.CRAFTING_TABLE)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(RegenObjects.Items.FOB_WATCH.get()).patternLine("QIG").patternLine("SES").patternLine("IGI").key('G', Items.GHAST_TEAR).key('I', Items.IRON_INGOT).key('E', Items.ENDER_EYE).key('S', Items.SPIDER_EYE).key('Q', Items.BLAZE_ROD).addCriterion("has_crafting_table", this.hasItem(Blocks.CRAFTING_TABLE)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(RegenObjects.Blocks.HAND_JAR.get()).patternLine("FOF").patternLine("FPF").patternLine("FOF").key('F', Items.IRON_INGOT).key('P', Items.ROTTEN_FLESH).key('O', Items.GLOWSTONE_DUST).addCriterion("has_crafting_table", this.hasItem(Blocks.CRAFTING_TABLE)).build(consumer);

        //Furnace
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(RegenObjects.Blocks.GAL_ORE.get()), RegenObjects.Items.GAL_INGOT.get(), 3.1F, 150).addCriterion("has_block", this.enteredBlock(RegenObjects.Blocks.GAL_ORE.get())).build(consumer, new ResourceLocation(Regeneration.MODID, "unknown_smelt"));
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(RegenObjects.Blocks.GAL_ORE.get()), RegenObjects.Items.GAL_INGOT.get(), 3.1F, 100).addCriterion("has_block", this.enteredBlock(RegenObjects.Blocks.GAL_ORE.get())).build(consumer, new ResourceLocation(Regeneration.MODID, "unknown_blast"));
    }
}
