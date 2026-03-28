package net.leahperson.proficientmod.registry;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.recipe.CookingRecipe;
import net.leahperson.proficientmod.recipe.CookingRecipeSerializer;
import net.leahperson.proficientmod.recipe.ForgingRecipe;
import net.leahperson.proficientmod.recipe.ForgingRecipeSerializer;
import net.leahperson.proficientmod.recipe.JewelcraftingRecipe;
import net.leahperson.proficientmod.recipe.JewelcraftingRecipeSerializer;
import net.leahperson.proficientmod.recipe.ReforgingRecipe;
import net.leahperson.proficientmod.recipe.ReforgingRecipeSerializer;
import net.leahperson.proficientmod.recipe.ScribingRecipe;
import net.leahperson.proficientmod.recipe.ScribingRecipeSerializer;
import net.leahperson.proficientmod.recipe.WorkbenchRecipe;
import net.leahperson.proficientmod.recipe.WorkbenchRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ProficientMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<ForgingRecipe>> FORGING =
            RECIPE_SERIALIZERS.register("forging", ForgingRecipeSerializer::new);

    public static final RegistryObject<RecipeSerializer<CookingRecipe>> COOKING =
            RECIPE_SERIALIZERS.register("cooking", CookingRecipeSerializer::new);

    public static final RegistryObject<RecipeSerializer<ScribingRecipe>> SCRIBING =
            RECIPE_SERIALIZERS.register("scribing", ScribingRecipeSerializer::new);

    public static final RegistryObject<RecipeSerializer<JewelcraftingRecipe>> JEWELCRAFTING =
            RECIPE_SERIALIZERS.register("jewelcrafting", JewelcraftingRecipeSerializer::new);

    public static final RegistryObject<RecipeSerializer<WorkbenchRecipe>> WORKBENCH =
            RECIPE_SERIALIZERS.register("workbench", WorkbenchRecipeSerializer::new);

    public static final RegistryObject<RecipeSerializer<ReforgingRecipe>> REFORGING =
            RECIPE_SERIALIZERS.register("reforging", ReforgingRecipeSerializer::new);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
}