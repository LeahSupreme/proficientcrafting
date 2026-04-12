package net.leahperson.proficientmod.item.food;

import net.leahperson.proficientmod.Config;
import net.leahperson.proficientmod.attribute.EffectAddition;
import net.leahperson.proficientmod.attribute.FoodAddition;
import net.leahperson.proficientmod.attribute.ItemQualityData;
import net.leahperson.proficientmod.quality.QualityDataType;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class QualityFoodItem extends Item {
    public record EffectDefinition(Supplier<MobEffect> effect, int baseAmplifier, int baseDuration, int durationPerRarity) {}

    @Nullable
    private final Item containerItem;
    private final List<EffectDefinition> effectDefinitions;

    public QualityFoodItem(@Nullable Item containerItem, List<EffectDefinition> effects, Properties properties) {
        super(properties);
        this.containerItem = containerItem;
        this.effectDefinitions = effects;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide) {
            int rarity = QualityDataUtil.getRarity(stack);
            for (EffectDefinition definition : effectDefinitions) {
                int amplifier = definition.baseAmplifier() + rarity;
                int duration = definition.baseDuration() + definition.durationPerRarity() * rarity;
                entity.addEffect(new MobEffectInstance(definition.effect().get(), duration, amplifier));
            }
        }
        if (containerItem != null && entity instanceof Player player && !player.getAbilities().instabuild) {
            if (result.isEmpty()) {
                return new ItemStack(containerItem);
            }
            player.getInventory().add(new ItemStack(containerItem));
        }
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (!Config.showFoodEffectTooltips) return;
        int rarity = QualityDataUtil.getRarity(stack);
        List<EffectAddition> rarityEffects = getRarityEffects(level, stack, rarity);

        if (effectDefinitions.isEmpty() && rarityEffects.isEmpty()) return;

        tooltip.add(Component.translatable("qualitycrafting.tooltip.food.when_consumed")
                .withStyle(ChatFormatting.GRAY));

        for (EffectDefinition definition : effectDefinitions) {
            MobEffect effect = definition.effect().get();
            int amplifier = definition.baseAmplifier() + rarity;
            int duration = definition.baseDuration() + definition.durationPerRarity() * rarity;
            tooltip.add(buildEffectLine(effect, amplifier, duration / 20));
        }

        for (EffectAddition addition : rarityEffects) {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.parse(addition.effectid()));
            if (effect == null) continue;
            tooltip.add(buildEffectLine(effect, addition.amplifier(), addition.duration()));
        }
    }

    private static MutableComponent buildEffectLine(MobEffect effect, int amplifier, int totalSeconds) {
        MutableComponent effectName = effect.getDisplayName().copy()
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(effect.getColor())));
        if (amplifier > 0) {
            effectName.append(" ").append(Component.translatable("enchantment.level." + (amplifier + 1)));
        }
        String durationText = totalSeconds / 60 + ":" + String.format("%02d", totalSeconds % 60);
        return Component.translatable("qualitycrafting.tooltip.food.effect", effectName, durationText)
                .withStyle(ChatFormatting.GRAY);
    }

    private static List<EffectAddition> getRarityEffects(@Nullable Level level, ItemStack stack, int rarity) {
        if (rarity == 0) return Collections.emptyList();
        Level resolvedLevel = level != null ? level : Minecraft.getInstance().level;
        if (resolvedLevel == null) return Collections.emptyList();

        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();
        String itemIdStr = itemId.getNamespace() + ":" + itemId.getPath();
        int rarityIndex = rarity - 1;
        Optional<Registry<ItemQualityData>> itemRegistry =
                resolvedLevel.registryAccess().registry(ItemQualityData.REGISTRY);
        if (itemRegistry.isPresent()) {
            Optional<List<EffectAddition>> perItemEffects = itemRegistry.get().stream()
                    .filter(entry -> entry.item_id().equals(itemIdStr))
                    .findAny()
                    .flatMap(ItemQualityData::food)
                    .filter(foodList -> rarityIndex < foodList.size())
                    .map(foodList -> foodList.get(rarityIndex).effectsadd());
            if (perItemEffects.isPresent()) {
                return perItemEffects.get();
            }
        }

        Optional<Registry<QualityDataType>> registry =
                resolvedLevel.registryAccess().registry(QualityDataType.RARITY_REGISTRY);
        return registry.map(qualityDataTypes -> qualityDataTypes.stream()
                .filter(tier -> tier.index() == rarity)
                .findFirst()
                .flatMap(QualityDataType::food)
                .map(FoodAddition::effectsadd)
                .orElse(Collections.emptyList())).orElse(Collections.emptyList());
    }
}
