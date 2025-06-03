package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.common.objects.RBlocks;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RegenRecipes extends RecipeProvider {
    public RegenRecipes(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RItems.FOB.get()).pattern("QIG").pattern("SES").pattern("IGI").define('G', Items.GHAST_TEAR).define('I', Items.IRON_INGOT).define('E', RItems.ZINC.get()).define('S', Items.SPIDER_EYE).define('Q', Items.BLAZE_ROD).group("regen").unlockedBy("has_crafting_table", has(Blocks.CRAFTING_TABLE)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RBlocks.ZERO_ROUNDEL.get()).pattern("   ").pattern("ZIZ").pattern("ZZZ").define('I', RItems.ZINC.get()).define('Z', ItemTags.STONE_BRICKS).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RBlocks.ZERO_ROOM_FULL.get()).pattern("ZZZ").pattern("ZIZ").pattern("ZZZ").define('I', RItems.ZINC.get()).define('Z', ItemTags.STONE_BRICKS).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RBlocks.AZBANTIUM.get(), 2).pattern("ZZZ").pattern("Z Z").pattern("ZZZ").define('Z', RItems.ZINC.get()).unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);

    }
}
