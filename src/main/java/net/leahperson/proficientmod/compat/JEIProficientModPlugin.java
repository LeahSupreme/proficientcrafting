package net.leahperson.proficientmod.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.recipe.CookingRecipe;
import net.leahperson.proficientmod.recipe.ForgingRecipe;
import net.leahperson.proficientmod.recipe.JewelcraftingRecipe;
import net.leahperson.proficientmod.recipe.ReforgingRecipe;
import net.leahperson.proficientmod.recipe.ScribingRecipe;
import net.leahperson.proficientmod.recipe.WorkbenchRecipe;
import net.leahperson.proficientmod.registry.ModRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIProficientModPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new ForgingTableCategory(guiHelper));
        registration.addRecipeCategories(new CookingPotCategory(guiHelper));
        registration.addRecipeCategories(new ScribingTableCategory(guiHelper));
        registration.addRecipeCategories(new JewelcraftingStationCategory(guiHelper));
        registration.addRecipeCategories(new WorkbenchCategory(guiHelper));
        registration.addRecipeCategories(new ReforgingAltarCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<ForgingRecipe> forgingRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.FORGING.get());
        registration.addRecipes(ForgingTableCategory.FORGING_TABLE_TYPE, forgingRecipes);

        List<CookingRecipe> cookingRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING.get());
        registration.addRecipes(CookingPotCategory.COOKING_POT_TYPE, cookingRecipes);

        List<ScribingRecipe> scribingRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.SCRIBING.get());
        registration.addRecipes(ScribingTableCategory.SCRIBING_TABLE_TYPE, scribingRecipes);

        List<JewelcraftingRecipe> jewelcraftingRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.JEWELCRAFTING.get());
        registration.addRecipes(JewelcraftingStationCategory.JEWELCRAFTING_STATION_TYPE, jewelcraftingRecipes);

        List<WorkbenchRecipe> workbenchRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.WORKBENCH.get());
        registration.addRecipes(WorkbenchCategory.WORKBENCH_TYPE, workbenchRecipes);

        List<ReforgingRecipe> reforgingRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.REFORGING.get());
        registration.addRecipes(ReforgingAltarCategory.REFORGING_ALTAR_TYPE, reforgingRecipes);
    }
}
