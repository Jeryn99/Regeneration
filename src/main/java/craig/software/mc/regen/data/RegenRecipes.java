package craig.software.mc.regen.data;

import craig.software.mc.regen.common.objects.RBlocks;
import craig.software.mc.regen.common.objects.RItems;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RegenRecipes extends RecipeProvider {
    public RegenRecipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RItems.FOB.get()).pattern("QIG").pattern("SES").pattern("IGI").define('G', Items.GHAST_TEAR).define('I', Items.IRON_INGOT).define('E', RItems.ZINC.get()).define('S', Items.SPIDER_EYE).define('Q', Items.BLAZE_ROD).group("regen").unlockedBy("has_crafting_table", has(Blocks.CRAFTING_TABLE)).save(consumer);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RBlocks.ZINC_ORE.get().asItem()), RItems.ZINC.get(), 0.7F, 300).unlockedBy("has_any_zinc", has(RBlocks.ZINC_ORE.get().asItem())).save(consumer, new ResourceLocation(RConstants.MODID, "smelt_zinc"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(RBlocks.ZINC_ORE.get().asItem()), RItems.ZINC.get(), 0.8F, 150).unlockedBy("has_any_zinc", has(RBlocks.ZINC_ORE.get().asItem())).save(consumer, new ResourceLocation(RConstants.MODID, "blast_zinc"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RBlocks.ZINC_ORE_DEEPSLATE.get().asItem()), RItems.ZINC.get(), 0.7F, 300).unlockedBy("has_any_zinc", has(RBlocks.ZINC_ORE_DEEPSLATE.get().asItem())).save(consumer, new ResourceLocation(RConstants.MODID, "smelt_zinc_deep"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(RBlocks.ZINC_ORE_DEEPSLATE.get().asItem()), RItems.ZINC.get(), 0.8F, 150).unlockedBy("has_any_zinc", has(RBlocks.ZINC_ORE_DEEPSLATE.get().asItem())).save(consumer, new ResourceLocation(RConstants.MODID, "blast_zinc_deep"));

        ShapedRecipeBuilder.shaped(RItems.GUARD_HELMET.get()).pattern("ZIZ").pattern("Z Z").pattern("   ").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.GUARD_CHEST.get()).pattern("Z Z").pattern("ZIZ").pattern("ZZZ").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.GUARD_LEGS.get()).pattern("ZIZ").pattern("Z Z").pattern("Z Z").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.GUARD_FEET.get()).pattern("   ").pattern("I I").pattern("Z Z").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);


        ShapedRecipeBuilder.shaped(RItems.F_ROBES_HEAD.get()).pattern("Z Z").pattern("ILI").pattern("Z Z").define('L', ItemTags.FLOWERS).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.F_ROBES_CHEST.get()).pattern("I I").pattern("ZLZ").pattern("ZZZ").define('L', ItemTags.FLOWERS).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.F_ROBES_LEGS.get()).pattern("L L").pattern("Z Z").pattern("I I").define('L', ItemTags.FLOWERS).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RItems.M_ROBES_HEAD.get()).pattern("Z Z").pattern("ILI").pattern("Z Z").define('L', Items.LEATHER).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.M_ROBES_CHEST.get()).pattern("I I").pattern("ZLZ").pattern("ZZZ").define('L', Items.LEATHER).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.M_ROBES_LEGS.get()).pattern("L L").pattern("Z Z").pattern("I I").define('L', Items.LEATHER).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RItems.ROBES_FEET.get()).pattern("   ").pattern("Z Z").pattern("I I").define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RBlocks.ZERO_ROUNDEL.get()).pattern("   ").pattern("ZIZ").pattern("ZZZ").define('I', RItems.ZINC.get()).define('Z', ItemTags.STONE_BRICKS).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RBlocks.ZERO_ROOM_FULL.get()).pattern("ZZZ").pattern("ZIZ").pattern("ZZZ").define('I', RItems.ZINC.get()).define('Z', ItemTags.STONE_BRICKS).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RBlocks.BIO_CONTAINER.get()).pattern("FZF").pattern("FPF").pattern("FOF").define('Z', RItems.ZINC.get()).define('F', Items.IRON_INGOT).define('P', Items.ROTTEN_FLESH).define('O', Items.GLOWSTONE_DUST).unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RBlocks.AZBANTIUM.get(), 2).pattern("ZZZ").pattern("Z Z").pattern("ZZZ").define('Z', RItems.ZINC.get()).unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);

    }
}
