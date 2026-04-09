package net.leahperson.proficientmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public class CookingRecipeSerializer implements RecipeSerializer<CookingRecipe> {

    @Override
    public CookingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonArray ingredientsJson = GsonHelper.getAsJsonArray(json, "ingredients");
        NonNullList<Ingredient> inputItems = NonNullList.withSize(ingredientsJson.size(), Ingredient.EMPTY);
        for (int i = 0; i < ingredientsJson.size(); i++) {
            inputItems.set(i, Ingredient.fromJson(ingredientsJson.get(i)));
        }

        ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);
        int cookTime = GsonHelper.getAsInt(json, "cookTime", 200);
        int levelCost = GsonHelper.getAsInt(json, "levelCost", 0);
        int proficiencyRequired = GsonHelper.getAsInt(json, "proficiency", 0);
        int yieldCost = GsonHelper.getAsInt(json, "yieldCost", 0);
        int yieldAdded = GsonHelper.getAsInt(json, "yieldAdded", 0);

        NonNullList<Integer> qualityRequired = NonNullList.withSize(0, 0);
        if (json.has("qualityCosts")) {
            JsonArray costsJson = GsonHelper.getAsJsonArray(json, "qualityCosts");
            qualityRequired = NonNullList.withSize(costsJson.size(), 0);
            for (int i = 0; i < costsJson.size(); i++) {
                qualityRequired.set(i, costsJson.get(i).getAsInt());
            }
        }

        NonNullList<ItemStack> outputs = NonNullList.withSize(qualityRequired.size() + 1, ItemStack.EMPTY);
        outputs.set(0, output);
        if (json.has("outputPerQuality") && !qualityRequired.isEmpty()) {
            JsonArray outputsJson = GsonHelper.getAsJsonArray(json, "outputPerQuality");
            for (int i = 0; i < Math.min(outputsJson.size(), qualityRequired.size()); i++) {
                outputs.set(i + 1, CraftingHelper.getItemStack(outputsJson.get(i).getAsJsonObject(), true));
            }
        }
        for (int i = 1; i < outputs.size(); i++) {
            if (outputs.get(i).isEmpty()) {
                outputs.set(i, output.copy());
            }
        }

        NonNullList<Float> qualityPerIngredient = NonNullList.withSize(0, 0f);
        if (json.has("qualityPerIngredient")) {
            JsonArray qpiJson = GsonHelper.getAsJsonArray(json, "qualityPerIngredient");
            qualityPerIngredient = NonNullList.withSize(qpiJson.size(), 0f);
            for (int i = 0; i < qpiJson.size(); i++) {
                qualityPerIngredient.set(i, qpiJson.get(i).getAsFloat());
            }
        }

        NonNullList<Integer> qualityDecoration = NonNullList.withSize(0, 0);
        if (json.has("qualityDecoration")) {
            JsonArray qdJson = GsonHelper.getAsJsonArray(json, "qualityDecoration");
            qualityDecoration = NonNullList.withSize(qdJson.size(), 0);
            for (int i = 0; i < qdJson.size(); i++) {
                qualityDecoration.set(i, qdJson.get(i).getAsInt());
            }
        }

        return new CookingRecipe(recipeId, inputItems, output, outputs, cookTime,
                qualityRequired, qualityPerIngredient, qualityDecoration, levelCost, proficiencyRequired, yieldCost, yieldAdded);
    }

    @Override
    public @Nullable CookingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        NonNullList<Ingredient> inputItems = NonNullList.withSize(size, Ingredient.EMPTY);
        for (int i = 0; i < size; i++) {
            inputItems.set(i, Ingredient.fromNetwork(buf));
        }

        ItemStack output = buf.readItem();
        int cookTime = buf.readVarInt();
        int levelCost = buf.readVarInt();
        int proficiencyRequired = buf.readVarInt();
        int yieldCost = buf.readVarInt();
        int yieldAdded = buf.readVarInt();

        int costsSize = buf.readVarInt();
        NonNullList<Integer> qualityRequired = NonNullList.withSize(costsSize, 0);
        for (int i = 0; i < costsSize; i++) {
            qualityRequired.set(i, buf.readVarInt());
        }

        int outputsSize = buf.readVarInt();
        NonNullList<ItemStack> outputs = NonNullList.withSize(outputsSize, ItemStack.EMPTY);
        for (int i = 0; i < outputsSize; i++) {
            outputs.set(i, buf.readItem());
        }

        int qualityPerIngredientSize = buf.readVarInt();
        NonNullList<Float> qualityPerIngredient = NonNullList.withSize(qualityPerIngredientSize, 0f);
        for (int i = 0; i < qualityPerIngredientSize; i++) {
            qualityPerIngredient.set(i, buf.readFloat());
        }

        int qualityDecorationSize = buf.readVarInt();
        NonNullList<Integer> qualityDecoration = NonNullList.withSize(qualityDecorationSize, 0);
        for (int i = 0; i < qualityDecorationSize; i++) {
            qualityDecoration.set(i, buf.readVarInt());
        }

        return new CookingRecipe(recipeId, inputItems, output, outputs, cookTime,
                qualityRequired, qualityPerIngredient, qualityDecoration, levelCost, proficiencyRequired, yieldCost, yieldAdded);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, CookingRecipe recipe) {
        buf.writeVarInt(recipe.getInputItems().size());
        for (Ingredient ingredient : recipe.getInputItems()) {
            ingredient.toNetwork(buf);
        }

        buf.writeItem(recipe.getResultItem(null));
        buf.writeVarInt(recipe.getCookTime());
        buf.writeVarInt(recipe.getLevelCost());
        buf.writeVarInt(recipe.getProficiencyRequired());
        buf.writeVarInt(recipe.getYieldCost());
        buf.writeVarInt(recipe.getYieldAdded());

        buf.writeVarInt(recipe.getQualityRequired().size());
        for (int cost : recipe.getQualityRequired()) {
            buf.writeVarInt(cost);
        }

        buf.writeVarInt(recipe.getOutputs().size());
        for (ItemStack output : recipe.getOutputs()) {
            buf.writeItem(output);
        }

        buf.writeVarInt(recipe.getQualityPerIngredient().size());
        for (float qualityPerIngredient : recipe.getQualityPerIngredient()) {
            buf.writeFloat(qualityPerIngredient);
        }

        buf.writeVarInt(recipe.getQualityDecoration().size());
        for (int qualityDecoration : recipe.getQualityDecoration()) {
            buf.writeVarInt(qualityDecoration);
        }
    }
}
