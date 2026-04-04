package net.leahperson.proficientmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
            for (JsonElement catalystElement : catalystsArray) {
                JsonObject catalystObject = catalystElement.getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(catalystObject);
                int count = GsonHelper.getAsInt(catalystObject, "count", 1);
                for (int copyIndex = 0; copyIndex < count; copyIndex++) {
                    catalysts.add(ingredient);
                }
            }
        }

        int craftTime = GsonHelper.getAsInt(json, "craftTime", 60);
        int proficiencyRequired = GsonHelper.getAsInt(json, "proficiency", 0);
        int levelCost = GsonHelper.getAsInt(json, "levelCost", 0);

        List<ReforgingAttributeDefinition> attributes = new ArrayList<>();
        if (json.has("attributes")) {
            JsonArray attributesArray = GsonHelper.getAsJsonArray(json, "attributes");
            for (JsonElement attributeElement : attributesArray) {
                attributes.add(parseReforgingAttribute(attributeElement.getAsJsonObject()));
            }
        }

        return new ReforgingRecipe(recipeId, targetIngredient, catalysts,
                attributes, craftTime, proficiencyRequired, levelCost);
    }

    @Override
    public @Nullable ReforgingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        boolean hasTarget = buffer.readBoolean();
        Optional<Ingredient> targetIngredient = hasTarget
                ? Optional.of(Ingredient.fromNetwork(buffer))
                : Optional.empty();

        int catalystCount = buffer.readVarInt();
        List<Ingredient> catalysts = new ArrayList<>(catalystCount);
        for (int catalystIndex = 0; catalystIndex < catalystCount; catalystIndex++) {
            catalysts.add(Ingredient.fromNetwork(buffer));
        }

        int craftTime = buffer.readVarInt();
        int proficiencyRequired = buffer.readVarInt();
        int levelCost = buffer.readVarInt();

        int attributeCount = buffer.readVarInt();
        List<ReforgingAttributeDefinition> attributes = new ArrayList<>(attributeCount);
        for (int attributeIndex = 0; attributeIndex < attributeCount; attributeIndex++) {
            String attributeId = buffer.readUtf();
            double avg0 = buffer.readDouble();
            double avg100 = buffer.readDouble();
            String operation = buffer.readUtf();
            attributes.add(new ReforgingAttributeDefinition(attributeId, avg0, avg100, operation));
        }

        return new ReforgingRecipe(recipeId, targetIngredient, catalysts,
                attributes, craftTime, proficiencyRequired, levelCost);
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

        buffer.writeVarInt(recipe.getAttributes().size());
        for (ReforgingAttributeDefinition definition : recipe.getAttributes()) {
            buffer.writeUtf(definition.attributeId());
            buffer.writeDouble(definition.avg0());
            buffer.writeDouble(definition.avg100());
            buffer.writeUtf(definition.operation());
        }
    }

    private static ReforgingAttributeDefinition parseReforgingAttribute(JsonObject json) {
        String attributeId = GsonHelper.getAsString(json, "attribute_id");
        double avg0 = GsonHelper.getAsDouble(json, "avg0");
        double avg100 = GsonHelper.getAsDouble(json, "avg100");
        String operation = GsonHelper.getAsString(json, "operation", "ADDITION");
        return new ReforgingAttributeDefinition(attributeId, avg0, avg100, operation);
    }
}
