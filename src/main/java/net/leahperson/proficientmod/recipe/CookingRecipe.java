package net.leahperson.proficientmod.recipe;

import net.leahperson.proficientmod.registry.ModRecipeSerializers;
import net.leahperson.proficientmod.registry.ModRecipeTypes;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

public class CookingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final NonNullList<ItemStack> outputs;
    private final int cookTime;
    private final NonNullList<Integer> qualityRequired;
    private final NonNullList<Float> qualityPerIngredient;
    private final NonNullList<Integer> qualityDecoration;
    private final int levelCost;
    private final int proficiencyRequired;
    private final int yieldCost;
    private final int yieldAdded;

    public CookingRecipe(ResourceLocation id, NonNullList<Ingredient> inputItems, ItemStack output,
                         NonNullList<ItemStack> outputs, int cookTime,
                         NonNullList<Integer> qualityRequired, NonNullList<Float> qualityPerIngredient,
                         NonNullList<Integer> qualityDecoration, int levelCost, int proficiencyRequired,
                         int yieldCost, int yieldAdded) {
        this.id = id;
        this.inputItems = inputItems;
        this.output = output;
        this.outputs = outputs;
        this.cookTime = cookTime;
        this.qualityRequired = qualityRequired;
        this.qualityPerIngredient = qualityPerIngredient;
        this.qualityDecoration = qualityDecoration;
        this.levelCost = levelCost;
        this.proficiencyRequired = proficiencyRequired;
        this.yieldCost = yieldCost;
        this.yieldAdded = yieldAdded;
    }

    public NonNullList<Ingredient> getInputItems() {
        return inputItems;
    }

    public NonNullList<ItemStack> getOutputs() {
        return outputs;
    }

    public int getCookTime() {
        return cookTime;
    }

    public NonNullList<Integer> getQualityRequired() {
        return qualityRequired;
    }

    public NonNullList<Float> getQualityPerIngredient() {
        return qualityPerIngredient;
    }

    public NonNullList<Integer> getQualityDecoration() {
        return qualityDecoration;
    }

    public int getLevelCost() {
        return levelCost;
    }

    public int getProficiencyRequired() {
        return proficiencyRequired;
    }

    public int getYieldCost() {
        return yieldCost;
    }

    public int getYieldAdded() {
        return yieldAdded;
    }

    public boolean hasQualityOutputs() {
        return !qualityRequired.isEmpty();
    }

    public ItemStack getOutputForQuality(int quality, RandomSource random) {
        int tier = 0;
        for (int i = 0; i < qualityRequired.size(); i++) {
            if (quality >= qualityRequired.get(i)) {
                tier = i + 1;
            }
        }
        if (tier < qualityRequired.size()) {
            int lowerThreshold = tier > 0 ? qualityRequired.get(tier - 1) : 0;
            int upperThreshold = qualityRequired.get(tier);
            float chance = (float) (quality - lowerThreshold) / (upperThreshold - lowerThreshold);
            if (random.nextFloat() < chance) {
                tier++;
            }
        }
        tier = Math.min(tier, outputs.size() - 1);
        ItemStack result = outputs.get(tier).copy();
        if (tier < qualityDecoration.size()) {
            QualityDataUtil.setRarity(result, qualityDecoration.get(tier));
        }
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public boolean matches(Container container, Level level) {
        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }
        return inputs.size() == inputItems.size()
                && RecipeMatcher.findMatches(inputs, inputItems) != null;
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
        return ModRecipeSerializers.COOKING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.COOKING.get();
    }
}
