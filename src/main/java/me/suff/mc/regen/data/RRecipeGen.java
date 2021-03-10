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
    protected void buildShapelessRecipes(Consumer< IFinishedRecipe > consumer) {
        ShapedRecipeBuilder.shaped(RItems.FOB.get()).pattern("QIG").pattern("SES").pattern("IGI").define('G', Items.GHAST_TEAR).define('I', Items.IRON_INGOT).define('E', RItems.ZINC.get()).define('S', Items.SPIDER_EYE).define('Q', Items.BLAZE_ROD).group("regen").unlockedBy("has_crafting_table", has(Blocks.CRAFTING_TABLE)).save(consumer);
        CookingRecipeBuilder.smelting(Ingredient.of(RBlocks.ZINC_ORE.get().asItem()), RItems.ZINC.get(), 0.7F, 300).unlockedBy("has_any_kontron", has(RBlocks.ZINC_ORE.get().asItem())).save(consumer, new ResourceLocation(RConstants.MODID, "smelt_zinc"));
        CookingRecipeBuilder.blasting(Ingredient.of(RBlocks.ZINC_ORE.get().asItem()), RItems.ZINC.get(), 0.8F, 150).unlockedBy("has_any_kontron", has(RBlocks.ZINC_ORE.get().asItem())).save(consumer, new ResourceLocation(RConstants.MODID, "blast_zinc"));

        ShapedRecipeBuilder.shaped(RItems.GUARD_HELMET.get()).pattern("ZIZ").pattern("Z Z").pattern("   ").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.GUARD_CHEST.get()).pattern("Z Z").pattern("ZIZ").pattern("ZZZ").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.GUARD_LEGS.get()).pattern("Z Z").pattern("ZIZ").pattern("ZZZ").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.GUARD_FEET.get()).pattern("ZIZ").pattern("Z Z").pattern("Z Z").define('I', Items.IRON_INGOT).define('Z', RItems.ZINC.get()).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);


        ShapedRecipeBuilder.shaped(RItems.F_ROBES_HEAD.get()).pattern("ZIZ").pattern("ZLZ").pattern("   ").define('L', ItemTags.FLOWERS).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.F_ROBES_CHEST.get()).pattern("Z Z").pattern("ZLZ").pattern("ZIZ").define('L', ItemTags.FLOWERS).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.F_ROBES_LEGS.get()).pattern("Z Z").pattern("ZLZ").pattern("ZIZ").define('L', ItemTags.FLOWERS).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RItems.M_ROBES_HEAD.get()).pattern("ZIZ").pattern("ZLZ").pattern("   ").define('L', Items.LEATHER).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.M_ROBES_CHEST.get()).pattern("Z Z").pattern("ZLZ").pattern("ZIZ").define('L', Items.LEATHER).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.M_ROBES_LEGS.get()).pattern("Z Z").pattern("ZLZ").pattern("ZIZ").define('L', Items.LEATHER).define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RItems.ROBES_FEET.get()).pattern("Z Z").pattern("Z Z").pattern("I I").define('I', RItems.ZINC.get()).define('Z', ItemTags.WOOL).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RBlocks.ZERO_ROUNDEL.get()).pattern("   ").pattern("ZIZ").pattern("ZZZ").define('I', RItems.ZINC.get()).define('Z', ItemTags.STONE_BRICKS).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RBlocks.ZERO_ROOM_FULL.get()).pattern("ZZZ").pattern("ZIZ").pattern("ZZZ").define('I', RItems.ZINC.get()).define('Z', ItemTags.STONE_BRICKS).group("regen").unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);
        ShapedRecipeBuilder.shaped(RBlocks.BIO_CONTAINER.get()).pattern("FZF").pattern("FPF").pattern("FOF").define('Z', RItems.ZINC.get()).define('F', Items.IRON_INGOT).define('P', Items.ROTTEN_FLESH).define('O', Items.GLOWSTONE_DUST).unlockedBy("has_zinc", has(RItems.ZINC.get())).save(consumer);


    }
}
