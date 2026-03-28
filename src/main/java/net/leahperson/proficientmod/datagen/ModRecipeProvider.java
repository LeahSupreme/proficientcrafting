package net.leahperson.proficientmod.datagen;

import net.leahperson.proficientmod.block.ModBlocks;
import net.leahperson.proficientmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FORGING_STATION.get())
                .pattern("CAS")
                .pattern(" I ")
                .pattern("III")
                .define('C', Items.CRAFTING_TABLE)
                .define('A', Items.ANVIL)
                .define('S', Items.SMITHING_TABLE)
                .define('I', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.ANVIL), has(Items.ANVIL))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COOKING_POT.get())
                .pattern("I I")
                .pattern("ICI")
                .pattern("IFI")
                .define('I', Items.IRON_INGOT)
                .define('C', Items.CAULDRON)
                .define('F', Items.CAMPFIRE)
                .unlockedBy(getHasName(Items.CAULDRON), has(Items.CAULDRON))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SCRIBING_TABLE.get())
                .pattern("I F")
                .pattern("PLP")
                .pattern("PPP")
                .define('P', ItemTags.PLANKS)
                .define('I', Items.INK_SAC)
                .define('L', Items.LECTERN)
                .define('F', Items.FEATHER)
                .unlockedBy(getHasName(Items.LECTERN), has(Items.LECTERN))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.JEWELCRAFTING_STATION.get())
                .pattern("DAD")
                .pattern("IGI")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GOLD_INGOT)
                .define('D', Items.DIAMOND)
                .define('A', Items.ANVIL)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WORKBENCH.get())
                .pattern("PCP")
                .pattern("LIL")
                .pattern("LIL")
                .define('P', ItemTags.PLANKS)
                .define('C', Items.CRAFTING_TABLE)
                .define('L', ItemTags.LOGS)
                .define('I', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.CRAFTING_TABLE), has(Items.CRAFTING_TABLE))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REFORGING_ALTAR.get())
                .pattern(" N ")
                .pattern("LEL")
                .pattern("OOO")
                .define('O', Items.OBSIDIAN)
                .define('N', Items.NETHER_STAR)
                .define('E', Items.ENCHANTING_TABLE)
                .define('L', Items.LAPIS_BLOCK)
                .unlockedBy(getHasName(Items.ENCHANTING_TABLE), has(Items.ENCHANTING_TABLE))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STONE_FORGE_HAMMER.get())
                .pattern("ASA")
                .pattern(" S ")
                .pattern(" S ")
                .define('A', Items.SMOOTH_STONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.SMOOTH_STONE), has(Items.SMOOTH_STONE))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WOODEN_LADLE.get())
                .pattern(" S ")
                .pattern(" S ")
                .pattern("PP ")
                .define('P', ItemTags.PLANKS)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.OAK_PLANKS), has(ItemTags.PLANKS))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_TIPPED_QUILL.get())
                .pattern(" F")
                .pattern("I ")
                .define('F', Items.FEATHER)
                .define('I', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_CHISEL.get())
                .pattern(" I ")
                .pattern(" I ")
                .pattern(" S ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_SAW.get())
                .pattern("  I")
                .pattern(" I ")
                .pattern("S  ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AMETHYST_SCEPTER.get())
                .pattern("  A")
                .pattern(" S ")
                .pattern("S  ")
                .define('A', Items.AMETHYST_BLOCK)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.AMETHYST_BLOCK), has(Items.AMETHYST_BLOCK))
                .save(pWriter);
    }
}
