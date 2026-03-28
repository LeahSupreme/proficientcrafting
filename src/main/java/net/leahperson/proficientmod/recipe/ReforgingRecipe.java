package net.leahperson.proficientmod.recipe;

import net.leahperson.proficientmod.attribute.AttributeAddition;
import net.leahperson.proficientmod.registry.ModRecipeSerializers;
import net.leahperson.proficientmod.registry.ModRecipeTypes;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReforgingRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final Optional<Ingredient> targetIngredient;
    private final List<Ingredient> catalysts;
    private final NonNullList<Integer> qualityRequired;
    private final List<List<ReforgingAttributeDefinition>> attributeTiers;
    private final NonNullList<Integer> qualityDecoration;
    private final int craftTime;
    private final int proficiencyRequired;
    private final int levelCost;

    public ReforgingRecipe(ResourceLocation id,
                           Optional<Ingredient> targetIngredient,
                           List<Ingredient> catalysts,
                           NonNullList<Integer> qualityRequired,
                           List<List<ReforgingAttributeDefinition>> attributeTiers,
                           NonNullList<Integer> qualityDecoration,
                           int craftTime,
                           int proficiencyRequired,
                           int levelCost) {
        this.id = id;
        this.targetIngredient = targetIngredient;
        this.catalysts = catalysts;
        this.qualityRequired = qualityRequired;
        this.attributeTiers = attributeTiers;
        this.qualityDecoration = qualityDecoration;
        this.craftTime = craftTime;
        this.proficiencyRequired = proficiencyRequired;
        this.levelCost = levelCost;
    }

    public Optional<Ingredient> getTargetIngredient() { return targetIngredient; }
    public List<Ingredient> getCatalysts() { return catalysts; }
    public NonNullList<Integer> getQualityRequired() { return qualityRequired; }
    public List<List<ReforgingAttributeDefinition>> getAttributeTiers() { return attributeTiers; }
    public NonNullList<Integer> getQualityDecoration() { return qualityDecoration; }
    public int getCraftTime() { return craftTime; }
    public int getProficiencyRequired() { return proficiencyRequired; }
    public int getLevelCost() { return levelCost; }

    public int getTierIndexForQuality(int qualityScore, RandomSource random) {
        if (qualityRequired.isEmpty()) {
            return 0;
        }
        int tierIndex = 0;
        for (int threshold = 0; threshold < qualityRequired.size(); threshold++) {
            if (qualityScore >= qualityRequired.get(threshold)) {
                tierIndex = threshold + 1;
            }
        }
        if (tierIndex < qualityRequired.size()) {
            int lowerBound = tierIndex > 0 ? qualityRequired.get(tierIndex - 1) : 0;
            int upperBound = qualityRequired.get(tierIndex);
            float progressFraction = (float) (qualityScore - lowerBound) / (upperBound - lowerBound);
            if (random.nextFloat() < progressFraction) {
                tierIndex++;
            }
        }
        return Math.min(tierIndex, attributeTiers.size() - 1);
    }

    public List<AttributeAddition> getAttributesForQuality(int qualityScore, RandomSource random) {
        int tierIndex = getTierIndexForQuality(qualityScore, random);
        if (tierIndex >= attributeTiers.size()) {
            return List.of();
        }
        return attributeTiers.get(tierIndex).stream()
                .map(def -> new AttributeAddition(
                        def.attributeId(),
                        def.min() + random.nextDouble() * (def.max() - def.min()),
                        def.operation()))
                .toList();
    }

    public int getRarityForTier(int tierIndex) {
        if (tierIndex < qualityDecoration.size()) {
            return qualityDecoration.get(tierIndex);
        }
        return tierIndex + 1;
    }

    @Override
    public boolean matches(Container container, Level level) {
        if (container.getContainerSize() < 5) return false;

        ItemStack targetStack = container.getItem(0);
        if (targetStack.isEmpty()) return false;
        if (targetIngredient.isPresent() && !targetIngredient.get().test(targetStack)) return false;

        List<ItemStack> faceItems = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            ItemStack slot = container.getItem(i);
            if (!slot.isEmpty()) faceItems.add(slot);
        }

        if (catalysts.size() != faceItems.size()) return false;

        List<ItemStack> remaining = new ArrayList<>(faceItems);
        for (Ingredient catalyst : catalysts) {
            boolean found = false;
            for (int i = 0; i < remaining.size(); i++) {
                if (catalyst.test(remaining.get(i))) {
                    remaining.remove(i);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(Container container, net.minecraft.core.RegistryAccess registryAccess) {
        return container.getItem(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredientList = NonNullList.create();
        ingredientList.addAll(catalysts);
        return ingredientList;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.REFORGING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.REFORGING.get();
    }
}
