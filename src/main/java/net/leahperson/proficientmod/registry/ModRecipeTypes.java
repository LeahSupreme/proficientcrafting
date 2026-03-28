package net.leahperson.proficientmod.registry;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.recipe.CookingRecipe;
import net.leahperson.proficientmod.recipe.ForgingRecipe;
import net.leahperson.proficientmod.recipe.JewelcraftingRecipe;
import net.leahperson.proficientmod.recipe.ReforgingRecipe;
import net.leahperson.proficientmod.recipe.ScribingRecipe;
import net.leahperson.proficientmod.recipe.WorkbenchRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ProficientMod.MOD_ID);

    public static final RegistryObject<RecipeType<ForgingRecipe>> FORGING =
            RECIPE_TYPES.register("forging", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return ProficientMod.MOD_ID + ":forging";
                }
            });

    public static final RegistryObject<RecipeType<CookingRecipe>> COOKING =
            RECIPE_TYPES.register("cooking", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return ProficientMod.MOD_ID + ":cooking";
                }
            });

    public static final RegistryObject<RecipeType<ScribingRecipe>> SCRIBING =
            RECIPE_TYPES.register("scribing", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return ProficientMod.MOD_ID + ":scribing";
                }
            });

    public static final RegistryObject<RecipeType<JewelcraftingRecipe>> JEWELCRAFTING =
            RECIPE_TYPES.register("jewelcrafting", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return ProficientMod.MOD_ID + ":jewelcrafting";
                }
            });

    public static final RegistryObject<RecipeType<WorkbenchRecipe>> WORKBENCH =
            RECIPE_TYPES.register("workbench", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return ProficientMod.MOD_ID + ":workbench";
                }
            });

    public static final RegistryObject<RecipeType<ReforgingRecipe>> REFORGING =
            RECIPE_TYPES.register("reforging", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return ProficientMod.MOD_ID + ":reforging";
                }
            });

    public static void register(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }
}