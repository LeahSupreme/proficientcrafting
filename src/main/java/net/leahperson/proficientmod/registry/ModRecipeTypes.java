package net.leahperson.proficientmod.registry;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.recipe.ForgingRecipe;
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

    public static void register(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }
}