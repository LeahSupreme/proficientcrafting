package net.leahperson.proficientmod.mixin.client;

import net.leahperson.proficientmod.attribute.EffectAddition;
import net.leahperson.proficientmod.attribute.FoodAddition;
import net.leahperson.proficientmod.attribute.ItemQualityData;
import net.leahperson.proficientmod.quality.QualityDataType;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void qualitycrafting$fixAttributeStyle(@Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> infoReturnable) {
        List<Component> tooltip = infoReturnable.getReturnValue();
        for (int i = 0; i < tooltip.size(); i++) {
            Component line = tooltip.get(i);
            if (!(line.getContents() instanceof TranslatableContents translatableContents)) continue;
            String key = translatableContents.getKey();
            if (!key.startsWith("attribute.modifier.plus.")) continue;
            Object[] args = translatableContents.getArgs();
            if (args.length < 2 || !(args[1] instanceof Component attributeComponent)) continue;
            if (!(attributeComponent.getContents() instanceof TranslatableContents attributeContent)) continue;
            if (!attributeContent.getKey().startsWith("qualitycrafting:")) continue;
            String newKey = "attribute.modifier.equals." + key.substring(key.lastIndexOf('.') + 1);
            tooltip.set(i, Component.literal(" ").append(Component.translatable(newKey, args).withStyle(ChatFormatting.DARK_GREEN)));
        }
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void qualitycrafting$addFoodQualityTooltip(@Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> infoReturnable) {
        ItemStack self = (ItemStack) (Object) this;
        if (!QualityUtils.hasQuality(self)) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        if (self.getItem().getFoodProperties(self, null) == null) {
            return;
        }

        FoodAddition foodAddition = qualitycrafting$resolveFoodAddition(self, minecraft.level.registryAccess());
        if (foodAddition == null) {
            return;
        }

        List<Component> tooltip = infoReturnable.getReturnValue();

        for (EffectAddition effectAddition : foodAddition.effectsadd()) {
            MobEffect mobEffect = minecraft.level.registryAccess()
                    .registryOrThrow(Registries.MOB_EFFECT)
                    .get(ResourceLocation.parse(effectAddition.effectid()));
            if (mobEffect == null) {
                continue;
            }
            Component effectName = Component.translatable(mobEffect.getDescriptionId());
            if (effectAddition.amplifier() > 0) {
                effectName = Component.translatable("potion.withAmplifier",
                        effectName,
                        Component.translatable("potion.potency." + effectAddition.amplifier()));
            }
            int minutes = effectAddition.duration() / 60;
            int seconds = effectAddition.duration() % 60;
            String durationText = String.format("%d:%02d", minutes, seconds);
            tooltip.add(Component.translatable("qualitycrafting.tooltip.food.effect", effectName, durationText)
                    .withStyle(style -> style.withColor(net.minecraft.network.chat.TextColor.fromRgb(mobEffect.getColor()))));
        }
    }

    @Unique
    private FoodAddition qualitycrafting$resolveFoodAddition(ItemStack stack, RegistryAccess registryAccess) {
        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();
        String itemIdStr = itemId.getNamespace() + ":" + itemId.getPath();
        int rarityIndex = QualityUtils.getQualityLevel(stack) - 1;

        Optional<ItemQualityData> itemData = registryAccess
                .registry(ItemQualityData.REGISTRY)
                .flatMap(registry -> registry.stream()
                        .filter(entry -> entry.item_id().equals(itemIdStr))
                        .findAny());
        if (itemData.isPresent() && itemData.get().food().isPresent()) {
            List<FoodAddition> foodList = itemData.get().food().get();
            if (rarityIndex < foodList.size()) {
                return foodList.get(rarityIndex);
            }
        }

        int targetIndex = rarityIndex + 1;
        Optional<QualityDataType> tier = registryAccess
                .registry(QualityDataType.RARITY_REGISTRY)
                .flatMap(registry -> registry.stream()
                        .filter(qualityDataType -> qualityDataType.index() == targetIndex)
                        .findFirst());
        if (tier.isPresent() && tier.get().food().isPresent()) {
            return tier.get().food().get();
        }

        return null;
    }
}
