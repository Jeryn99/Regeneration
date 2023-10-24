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
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RItems.FOB.get()).pattern("QIG").pattern("SES").pattern("IGI").define('G', Items.GHAST_TEAR).define('I', Items.IRON_INGOT).define('E', RItems.ZINC.get()).define('S', Items.SPIDER_EYE).define('Q', Items.BLAZE_ROD).group("regen").unlockedBy("has_crafting_table", has(Blocks.CRAFTING_TABLE)).save(consumer);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RBlocks.ZINC_ORE.get().asItem()), RecipeCategory.MISC, RItems.ZINC.get(), 0.7F, 300).unlockedBy("has_any_zinc", has(RBlocks.ZINC_ORE.get().asItem())).save(consumer, new ResourceLocation(RConstants.MODID, "smelt_zinc"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(RBlocks.ZINC_ORE.get().asItem()), RecipeCategory.MISC, RItems.ZINC.get(), 0.8F, 150).unlockedBy("has_any_zinc", has(RBlocks.ZINC_ORE.get().asItem())).save(consumer, new ResourceLocation(RConstants.MODID, "blast_zinc"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RBlocks.ZINC_ORE_DEEPSLATE.get().asItem()), RecipeCategory.MISC, RItems.ZINC.get(), 0.7F, 300).unlockedBy("has_any_zinc", has(RBlocks.ZINC_ORE_DEEPSLATE.get().asItem())).save(consumer, new ResourceLocation(RConstants.MODID, "smelt_zinc_deep"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(RBlocks.ZINC_ORE_DEEPSLATE.get().asItem()), RecipeCategory.MISC, RItems.ZINC.get(), 0.8F, 150).unlockedBy("has_any_zinc", has(RBlocks.ZINC_ORE_DEEPSLATE.get().asItem())).save(consumer, new ResourceLocation(RConstants.MODID, "blast_zinc_deep"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.GUARD_HELMET.get()).pattern("ZIZ").pattern("Z Z").pattern("   ").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.GUARD_CHEST.get()).pattern("Z Z").pattern("ZIZ").pattern("ZZZ").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.GUARD_LEGS.get()).pattern("ZIZ").pattern("Z Z").pattern("Z Z").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.GUARD_FEET.get()).pattern("   ").pattern("I I").pattern("Z Z").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.F_ROBES_HEAD.get()).pattern("Z Z").pattern("ILI").pattern("Z Z").define('L', ItemTags.FLOWERS).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.F_ROBES_CHEST.get()).pattern("I I").pattern("ZLZ").pattern("ZZZ").define('L', ItemTags.FLOWERS).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.F_ROBES_LEGS.get()).pattern("L L").pattern("Z Z").pattern("I I").define('L', ItemTags.FLOWERS).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.M_ROBES_HEAD.get()).pattern("Z Z").pattern("ILI").pattern("Z Z").define('L', Items.LEATHER).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.M_ROBES_CHEST.get()).pattern("I I").pattern("ZLZ").pattern("ZZZ").define('L', Items.LEATHER).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.M_ROBES_LEGS.get()).pattern("L L").pattern("Z Z").pattern("I I").define('L', Items.LEATHER).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RItems.ROBES_FEET.get()).pattern("   ").pattern("Z Z").pattern("I I").define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RBlocks.ZERO_ROUNDEL.get()).pattern("   ").pattern("ZIZ").pattern("ZZZ").define('I', RItems.ZINC.get()).define('Z', ItemTags.STONE_BRICKS).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RBlocks.ZERO_ROOM_FULL.get()).pattern("ZZZ").pattern("ZIZ").pattern("ZZZ").define('I', RItems.ZINC.get()).define('Z', ItemTags.STONE_BRICKS).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RBlocks.BIO_CONTAINER.get()).pattern("FZF").pattern("FPF").pattern("FOF").define('Z', RItems.ZINC.get()).define('F', Items.IRON_INGOT).define('P', Items.ROTTEN_FLESH).define('O', Items.GLOWSTONE_DUST).unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RBlocks.AZBANTIUM.get(), 2).pattern("ZZZ").pattern("Z Z").pattern("ZZZ").define('Z', RItems.ZINC.get()).unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, RItems.PLASMA_CARTRIDGE.get(), 20).requires(RItems.ZINC.get()).requires(Items.IRON_INGOT).requires(Items.GUNPOWDER).unlockedBy("has_zinc", has(RBlocks.ZINC_ORE.get())).save(consumer, "zinc");

    }
}
