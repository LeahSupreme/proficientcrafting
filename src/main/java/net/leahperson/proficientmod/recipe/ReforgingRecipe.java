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
    private final List<ReforgingAttributeDefinition> attributes;
    private final int craftTime;
    private final int proficiencyRequired;
    private final int levelCost;

    public ReforgingRecipe(ResourceLocation id,
                           Optional<Ingredient> targetIngredient,
                           List<Ingredient> catalysts,
                           List<ReforgingAttributeDefinition> attributes,
                           int craftTime,
                           int proficiencyRequired,
                           int levelCost) {
        this.id = id;
        this.targetIngredient = targetIngredient;
        this.catalysts = catalysts;
        this.attributes = attributes;
        this.craftTime = craftTime;
        this.proficiencyRequired = proficiencyRequired;
        this.levelCost = levelCost;
    }

    public Optional<Ingredient> getTargetIngredient() {
        return targetIngredient;
    }

    public List<Ingredient> getCatalysts() {
        return catalysts;
    }

    public List<ReforgingAttributeDefinition> getAttributes() {
        return attributes;
    }

    public int getCraftTime() {
        return craftTime;
    }

    public int getProficiencyRequired() {
        return proficiencyRequired;
    }

    public int getLevelCost() {
        return levelCost;
    }

    public List<AttributeAddition> getAttributesForQuality(int quality, RandomSource random) {
        List<AttributeAddition> result = new ArrayList<>(attributes.size());
        for (ReforgingAttributeDefinition definition : attributes) {
            double averageAtQuality = Math.sqrt(quality / 100.0) * (definition.avg100() - definition.avg0()) + definition.avg0();
            double standardDeviation = averageAtQuality * Math.sqrt(Math.PI / 2.0);
            double amount = Math.floor(Math.abs(random.nextGaussian() * standardDeviation));
            result.add(new AttributeAddition(definition.attributeId(), amount, definition.operation()));
        }
        return result;
    }

    @Override
    public boolean matches(Container container, Level level) {
        if (container.getContainerSize() < 5) {
            return false;
        }

        ItemStack targetStack = container.getItem(0);
        if (targetStack.isEmpty()) {
            return false;
        }
        if (targetIngredient.isPresent() && !targetIngredient.get().test(targetStack)) {
            return false;
        }

        List<ItemStack> faceItems = new ArrayList<>();
        for (int slotIndex = 1; slotIndex < 5; slotIndex++) {
            ItemStack slot = container.getItem(slotIndex);
            if (!slot.isEmpty()) {
                faceItems.add(slot);
            }
        }

        if (catalysts.size() != faceItems.size()) {
            return false;
        }

        List<ItemStack> remaining = new ArrayList<>(faceItems);
        for (Ingredient catalyst : catalysts) {
            boolean found = false;
            for (int remainingIndex = 0; remainingIndex < remaining.size(); remainingIndex++) {
                if (catalyst.test(remaining.get(remainingIndex))) {
                    remaining.remove(remainingIndex);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
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
