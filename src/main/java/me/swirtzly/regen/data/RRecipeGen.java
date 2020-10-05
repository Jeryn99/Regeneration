package me.swirtzly.regen.data;

import me.swirtzly.regen.common.objects.RItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class RRecipeGen extends RecipeProvider {
    public RRecipeGen(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(RItems.FOB.get()).patternLine("QIG").patternLine("SES").patternLine("IGI").key('G', Items.GHAST_TEAR).key('I', Items.IRON_INGOT).key('E', Items.ENDER_EYE).key('S', Items.SPIDER_EYE).key('Q', Items.BLAZE_ROD).addCriterion("has_crafting_table", this.hasItem(Blocks.CRAFTING_TABLE)).build(consumer);
    }
}
