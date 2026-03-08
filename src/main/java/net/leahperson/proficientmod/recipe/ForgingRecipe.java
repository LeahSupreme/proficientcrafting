package net.leahperson.proficientmod.recipe;

import net.leahperson.proficientmod.registry.ModRecipeSerializers;
import net.leahperson.proficientmod.registry.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class ForgingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack output;
    private final int minQuality;
    private final int craftTime;

    public ForgingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack output, int minQuality, int craftTime) {
        this.id = id;
        this.ingredients = ingredients;
        this.output = output;
        this.minQuality = minQuality;
        this.craftTime = craftTime;
    }

    public int getMinQuality() {
        return minQuality;
    }

    public int getCraftTime() {
        return craftTime;
    }

    public NonNullList<Ingredient> getIngredientsList() {
        return ingredients;
    }

    @Override
    public boolean matches(Container container, Level level) {
        if (container.getContainerSize() < 9) return false;

        for (int i = 0; i < 9; i++) {
            Ingredient ingredient = ingredients.get(i);
            ItemStack stack = container.getItem(i);

            if (!ingredient.test(stack)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(Container container, net.minecraft.core.RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.FORGING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.FORGING.get();
    }
}