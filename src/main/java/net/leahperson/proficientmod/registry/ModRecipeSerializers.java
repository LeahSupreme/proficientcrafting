package net.leahperson.proficientmod.registry;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.recipe.ForgingRecipe;
import net.leahperson.proficientmod.recipe.ForgingRecipeSerializer;
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

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
}