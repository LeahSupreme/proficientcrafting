package net.leahperson.proficientmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class ForgingRecipeSerializer implements RecipeSerializer<ForgingRecipe> {

    @Override
    public ForgingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonArray ingredientsJson = GsonHelper.getAsJsonArray(json, "ingredients");
        if (ingredientsJson.size() != 9) {
            throw new IllegalArgumentException("Forging recipe must have exactly 9 ingredients: " + recipeId);
        }

        NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
        for (int i = 0; i < 9; i++) {
            ingredients.set(i, Ingredient.fromJson(ingredientsJson.get(i)));
        }

        ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
        int minQuality = GsonHelper.getAsInt(json, "min_quality", 0);
        int craftTime = GsonHelper.getAsInt(json, "craft_time", 100);

        return new ForgingRecipe(recipeId, ingredients, output, minQuality, craftTime);
    }

    @Override
    public @Nullable ForgingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
        for (int i = 0; i < 9; i++) {
            ingredients.set(i, Ingredient.fromNetwork(buf));
        }

        ItemStack output = buf.readItem();
        int minQuality = buf.readVarInt();
        int craftTime = buf.readVarInt();

        return new ForgingRecipe(recipeId, ingredients, output, minQuality, craftTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, ForgingRecipe recipe) {
        for (Ingredient ingredient : recipe.getIngredientsList()) {
            ingredient.toNetwork(buf);
        }

        buf.writeItem(recipe.getResultItem(null));
        buf.writeVarInt(recipe.getMinQuality());
        buf.writeVarInt(recipe.getCraftTime());
    }
}