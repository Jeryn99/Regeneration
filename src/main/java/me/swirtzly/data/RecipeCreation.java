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
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {

        // Crafting Table
        ShapedRecipeBuilder.shaped(RegenObjects.Items.SEAL.get()).pattern(" G ").pattern("G G").pattern(" G ").define('G', RegenObjects.Items.GAL_INGOT.get()).unlocks("has_crafting_table", this.has(Blocks.CRAFTING_TABLE)).save(consumer);
        ShapedRecipeBuilder.shaped(RegenObjects.Items.FOB_WATCH.get()).pattern("QIG").pattern("SES").pattern("IGI").define('G', Items.GHAST_TEAR).define('I', Items.IRON_INGOT).define('E', Items.ENDER_EYE).define('S', Items.SPIDER_EYE).define('Q', Items.BLAZE_ROD).unlocks("has_crafting_table", this.has(Blocks.CRAFTING_TABLE)).save(consumer);
        ShapedRecipeBuilder.shaped(RegenObjects.Blocks.HAND_JAR.get()).pattern("FOF").pattern("FPF").pattern("FOF").define('F', Items.IRON_INGOT).define('P', Items.ROTTEN_FLESH).define('O', Items.GLOWSTONE_DUST).unlocks("has_crafting_table", this.has(Blocks.CRAFTING_TABLE)).save(consumer);

        //Furnace
        CookingRecipeBuilder.smelting(Ingredient.of(RegenObjects.Blocks.GAL_ORE.get()), RegenObjects.Items.GAL_INGOT.get(), 3.1F, 150).unlocks("has_block", this.insideOf(RegenObjects.Blocks.GAL_ORE.get())).save(consumer, new ResourceLocation(Regeneration.MODID, "unknown_smelt"));
        CookingRecipeBuilder.blasting(Ingredient.of(RegenObjects.Blocks.GAL_ORE.get()), RegenObjects.Items.GAL_INGOT.get(), 3.1F, 100).unlocks("has_block", this.insideOf(RegenObjects.Blocks.GAL_ORE.get())).save(consumer, new ResourceLocation(Regeneration.MODID, "unknown_blast"));
    }
}
