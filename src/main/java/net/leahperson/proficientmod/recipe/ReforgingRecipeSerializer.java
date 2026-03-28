package net.leahperson.proficientmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReforgingRecipeSerializer implements RecipeSerializer<ReforgingRecipe> {

    @Override
    public ReforgingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Optional<Ingredient> targetIngredient = json.has("target")
                ? Optional.of(Ingredient.fromJson(json.get("target")))
                : Optional.empty();

        List<Ingredient> catalysts = new ArrayList<>();
        if (json.has("catalysts")) {
            JsonArray catalystsArray = GsonHelper.getAsJsonArray(json, "catalysts");
            for (JsonElement element : catalystsArray) {
                JsonObject catalystObj = element.getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(catalystObj);
                int count = GsonHelper.getAsInt(catalystObj, "count", 1);
                for (int i = 0; i < count; i++) {
                    catalysts.add(ingredient);
                }
            }
        }

        int craftTime           = GsonHelper.getAsInt(json, "craftTime", 60);
        int proficiencyRequired = GsonHelper.getAsInt(json, "proficiency", 0);
        int levelCost           = GsonHelper.getAsInt(json, "levelCost", 0);

        NonNullList<Integer> qualityRequired = NonNullList.withSize(0, 0);
        if (json.has("qualityCosts")) {
            JsonArray qualityCostsArray = GsonHelper.getAsJsonArray(json, "qualityCosts");
            qualityRequired = NonNullList.withSize(qualityCostsArray.size(), 0);
            for (int index = 0; index < qualityCostsArray.size(); index++) {
                qualityRequired.set(index, qualityCostsArray.get(index).getAsInt());
            }
        }

        List<List<ReforgingAttributeDefinition>> attributeTiers = new ArrayList<>();
        if (json.has("attributeTiers")) {
            JsonArray tiersArray = GsonHelper.getAsJsonArray(json, "attributeTiers");
            for (JsonElement tierElement : tiersArray) {
                JsonArray tierArray = tierElement.getAsJsonArray();
                List<ReforgingAttributeDefinition> tier = new ArrayList<>();
                for (JsonElement attributeElement : tierArray) {
                    tier.add(parseReforgingAttribute(attributeElement.getAsJsonObject()));
                }
                attributeTiers.add(tier);
            }
        }

        NonNullList<Integer> qualityDecoration = NonNullList.withSize(0, 0);
        if (json.has("qualityDecoration")) {
            JsonArray decorationArray = GsonHelper.getAsJsonArray(json, "qualityDecoration");
            qualityDecoration = NonNullList.withSize(decorationArray.size(), 0);
            for (int index = 0; index < decorationArray.size(); index++) {
                qualityDecoration.set(index, decorationArray.get(index).getAsInt());
            }
        }

        return new ReforgingRecipe(recipeId, targetIngredient, catalysts,
                qualityRequired, attributeTiers, qualityDecoration,
                craftTime, proficiencyRequired, levelCost);
    }

    @Override
    public @Nullable ReforgingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        boolean hasTarget = buffer.readBoolean();
        Optional<Ingredient> targetIngredient = hasTarget
                ? Optional.of(Ingredient.fromNetwork(buffer))
                : Optional.empty();

        int catalystCount = buffer.readVarInt();
        List<Ingredient> catalysts = new ArrayList<>(catalystCount);
        for (int i = 0; i < catalystCount; i++) {
            catalysts.add(Ingredient.fromNetwork(buffer));
        }

        int craftTime           = buffer.readVarInt();
        int proficiencyRequired = buffer.readVarInt();
        int levelCost           = buffer.readVarInt();

        int qualityThresholdCount = buffer.readVarInt();
        NonNullList<Integer> qualityRequired = NonNullList.withSize(qualityThresholdCount, 0);
        for (int index = 0; index < qualityThresholdCount; index++) {
            qualityRequired.set(index, buffer.readVarInt());
        }

        int tierCount = buffer.readVarInt();
        List<List<ReforgingAttributeDefinition>> attributeTiers = new ArrayList<>(tierCount);
        for (int tierIndex = 0; tierIndex < tierCount; tierIndex++) {
            int attributeCount = buffer.readVarInt();
            List<ReforgingAttributeDefinition> tier = new ArrayList<>(attributeCount);
            for (int attributeIndex = 0; attributeIndex < attributeCount; attributeIndex++) {
                String attributeId = buffer.readUtf();
                double min         = buffer.readDouble();
                double max         = buffer.readDouble();
                String operation   = buffer.readUtf();
                tier.add(new ReforgingAttributeDefinition(attributeId, min, max, operation));
            }
            attributeTiers.add(tier);
        }

        int decorationCount = buffer.readVarInt();
        NonNullList<Integer> qualityDecoration = NonNullList.withSize(decorationCount, 0);
        for (int index = 0; index < decorationCount; index++) {
            qualityDecoration.set(index, buffer.readVarInt());
        }

        return new ReforgingRecipe(recipeId, targetIngredient, catalysts,
                qualityRequired, attributeTiers, qualityDecoration,
                craftTime, proficiencyRequired, levelCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ReforgingRecipe recipe) {
        buffer.writeBoolean(recipe.getTargetIngredient().isPresent());
        recipe.getTargetIngredient().ifPresent(ingredient -> ingredient.toNetwork(buffer));

        buffer.writeVarInt(recipe.getCatalysts().size());
        for (Ingredient catalyst : recipe.getCatalysts()) {
            catalyst.toNetwork(buffer);
        }

        buffer.writeVarInt(recipe.getCraftTime());
        buffer.writeVarInt(recipe.getProficiencyRequired());
        buffer.writeVarInt(recipe.getLevelCost());

        buffer.writeVarInt(recipe.getQualityRequired().size());
        for (int threshold : recipe.getQualityRequired()) {
            buffer.writeVarInt(threshold);
        }

        buffer.writeVarInt(recipe.getAttributeTiers().size());
        for (List<ReforgingAttributeDefinition> tier : recipe.getAttributeTiers()) {
            buffer.writeVarInt(tier.size());
            for (ReforgingAttributeDefinition def : tier) {
                buffer.writeUtf(def.attributeId());
                buffer.writeDouble(def.min());
                buffer.writeDouble(def.max());
                buffer.writeUtf(def.operation());
            }
        }

        buffer.writeVarInt(recipe.getQualityDecoration().size());
        for (int decoration : recipe.getQualityDecoration()) {
            buffer.writeVarInt(decoration);
        }
    }

    private static ReforgingAttributeDefinition parseReforgingAttribute(JsonObject json) {
        String attributeId = GsonHelper.getAsString(json, "attribute_id");
        double min         = GsonHelper.getAsDouble(json, "min");
        double max         = GsonHelper.getAsDouble(json, "max");
        String operation   = GsonHelper.getAsString(json, "operation", "ADDITION");
        return new ReforgingAttributeDefinition(attributeId, min, max, operation);
    }
}
