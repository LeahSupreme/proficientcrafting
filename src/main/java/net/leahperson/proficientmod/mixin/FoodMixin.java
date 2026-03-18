package net.leahperson.proficientmod.mixin;

import net.leahperson.proficientmod.attribute.FoodAddition;
import net.leahperson.proficientmod.attribute.ItemQualityData;
import net.leahperson.proficientmod.quality.QualityDataType;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.Optional;

@Mixin(Item.class)
public abstract class FoodMixin implements IForgeItem {

    @Override
    public FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        if (!QualityUtils.hasQuality(stack)) {
            return IForgeItem.super.getFoodProperties(stack, entity);
        }

        if (IForgeItem.super.getFoodProperties(stack, entity) == null) {
            return null;
        }

        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();
        String itemIdStr = itemId.getNamespace() + ":" + itemId.getPath();
        int rarityIndex = QualityUtils.getQualityLevel(stack) - 1;

        FoodAddition foodAddition = findPerItemFoodAddition(itemIdStr, rarityIndex);
        if (foodAddition == null) {
            foodAddition = findDefaultFoodAddition(rarityIndex);
        }
        if (foodAddition == null) {
            return IForgeItem.super.getFoodProperties(stack, entity);
        }

        return buildFoodProperties(stack, entity, foodAddition);
    }

    private FoodAddition findPerItemFoodAddition(String itemIdStr, int rarityIndex) {
        Optional<ItemQualityData> itemData = Minecraft.getInstance().level.registryAccess()
                .registry(ItemQualityData.REGISTRY)
                .flatMap(registry -> registry.stream()
                        .filter(entry -> entry.item_id().equals(itemIdStr))
                        .findAny());
        if (itemData.isEmpty() || itemData.get().food().isEmpty()) {
            return null;
        }
        List<FoodAddition> foodList = itemData.get().food().get();
        if (rarityIndex >= foodList.size()) {
            return null;
        }
        return foodList.get(rarityIndex);
    }

    private FoodAddition findDefaultFoodAddition(int rarityIndex) {
        Optional<net.minecraft.core.Registry<QualityDataType>> registry = Minecraft.getInstance().level.registryAccess()
                .registry(QualityDataType.RARITY_REGISTRY);
        if (registry.isEmpty()) {
            return null;
        }
        int targetIndex = rarityIndex + 1;
        Optional<QualityDataType> tier = registry.get().stream()
                .filter(qualityDataType -> qualityDataType.index() == targetIndex)
                .findFirst();
        if (tier.isEmpty() || tier.get().food().isEmpty()) {
            return null;
        }
        return tier.get().food().get();
    }

    private FoodProperties buildFoodProperties(ItemStack stack, @Nullable LivingEntity entity, FoodAddition foodAddition) {
        FoodProperties baseProperties = IForgeItem.super.getFoodProperties(stack, entity);
        FoodProperties.Builder builder = new FoodProperties.Builder();

        builder.nutrition((int) (baseProperties.getNutrition() + foodAddition.nutritionadd()))
                .saturationMod(baseProperties.getSaturationModifier() + foodAddition.saturationmodifieradd());

        if (baseProperties.isFastFood()) {
            builder.fast();
        }
        if (baseProperties.canAlwaysEat()) {
            builder.alwaysEat();
        }
        if (baseProperties.isMeat()) {
            builder.meat();
        }

        baseProperties.getEffects().forEach(effect -> builder.effect(effect.getFirst(), effect.getSecond()));

        foodAddition.effectsadd().forEach(effect -> builder.effect(
                new MobEffectInstance(
                        Minecraft.getInstance().level.registryAccess()
                                .registryOrThrow(Registries.MOB_EFFECT)
                                .get(ResourceLocation.parse(effect.effectid())),
                        effect.duration() * 20,
                        effect.amplifier()),
                1.0f));

        return builder.build();
    }
}
