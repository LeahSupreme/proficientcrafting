package net.leahperson.proficientmod.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.Config;
import net.leahperson.proficientmod.attribute.AttributeAddition;
import net.leahperson.proficientmod.attribute.ItemQualityData;
import net.leahperson.proficientmod.enchantment.ModEnchantments;
import net.leahperson.proficientmod.util.ModTags;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.leahperson.proficientmod.util.AttributeUtils;
import net.leahperson.proficientmod.util.RarityAttributeNBT;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ProficientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class QualityItemAttributeEvent {

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack output = event.getCrafting();
        if (output.isEmpty()) return;

        net.minecraft.world.Container inventory = event.getInventory();
        int minRarity = Integer.MAX_VALUE;
        boolean anyQuality = false;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack ingredient = inventory.getItem(i);
            if (!ingredient.isEmpty() && QualityUtils.hasQuality(ingredient)) {
                int rarity = QualityUtils.getQualityLevel(ingredient);
                if (rarity > 0) {
                    minRarity = Math.min(minRarity, rarity);
                    anyQuality = true;
                }
            }
        }

        if (anyQuality && minRarity != Integer.MAX_VALUE) {
            QualityDataUtil.setRarity(output, minRarity);
        }
    }

    @SubscribeEvent
    public static void addQualityTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!QualityUtils.hasQuality(stack) && !RarityAttributeNBT.hasAttributes(stack)) return;
        int quality = QualityUtils.getQualityLevel(stack);
        List<Component> tooltip = event.getToolTip();
        if (tooltip.isEmpty()) return;

        if (quality > 0) {
            String itemName = tooltip.get(0).getString();
            tooltip.set(0, QualityUtils.styleRarity(quality, QualityUtils.getRarityName(quality) + " " + itemName));
        }
    }

    @SubscribeEvent
    public static void addQualityCurioAttributes(CurioAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();

        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();

        List<AttributeAddition> attributeAdditions;
        if (RarityAttributeNBT.hasAttributes(stack)) {
            attributeAdditions = RarityAttributeNBT.getAttributes(stack);
        } else if (QualityUtils.hasQuality(stack)) {
            RegistryAccess registryAccess = event.getSlotContext().entity().level().registryAccess();
            Optional<ItemQualityData> qualityEntry = registryAccess
                    .registryOrThrow(ItemQualityData.REGISTRY)
                    .stream()
                    .filter(d -> d.item_id().equals(itemId.getNamespace() + ":" + itemId.getPath()))
                    .findAny();
            if (qualityEntry.isEmpty() || qualityEntry.get().rarities().isEmpty()) return;
            int rarityIndex = QualityUtils.getQualityLevel(stack) - 1;
            List<List<AttributeAddition>> rarities = qualityEntry.get().rarities().get();
            if (rarityIndex < 0 || rarityIndex >= rarities.size()) return;
            attributeAdditions = rarities.get(rarityIndex);
        } else {
            return;
        }

        if (attributeAdditions.isEmpty()) return;

        Multimap<Attribute, AttributeModifier> qualityModifiers = buildModifiers(attributeAdditions, itemId, QualityUtils.getQualityLevel(stack));
        Multimap<Attribute, AttributeModifier> combined = AttributeUtils.combineAttributes(event.getOriginalModifiers(), qualityModifiers);
        event.clearModifiers();
        combined.forEach(event::addModifier);
    }

    @SubscribeEvent
    public static void addQualityToolAttributes(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!LivingEntity.getEquipmentSlotForItem(stack).equals(event.getSlotType())) return;

        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();

        List<AttributeAddition> attributeAdditions;
        if (RarityAttributeNBT.hasAttributes(stack)) {
            attributeAdditions = RarityAttributeNBT.getAttributes(stack);
        } else if (QualityUtils.hasQuality(stack)) {
            var server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) return;
            RegistryAccess registryAccess = server.registryAccess();
            Optional<ItemQualityData> qualityEntry = registryAccess
                    .registryOrThrow(ItemQualityData.REGISTRY)
                    .stream()
                    .filter(d -> d.item_id().equals(itemId.getNamespace() + ":" + itemId.getPath()))
                    .findAny();
            if (qualityEntry.isEmpty() || qualityEntry.get().rarities().isEmpty()) return;
            int rarityIndex = QualityUtils.getQualityLevel(stack) - 1;
            List<List<AttributeAddition>> rarities = qualityEntry.get().rarities().get();
            if (rarityIndex < 0 || rarityIndex >= rarities.size()) return;
            attributeAdditions = rarities.get(rarityIndex);
        } else {
            return;
        }

        if (attributeAdditions.isEmpty()) return;

        Multimap<Attribute, AttributeModifier> qualityModifiers = buildModifiers(attributeAdditions, itemId, QualityUtils.getQualityLevel(stack));
        Multimap<Attribute, AttributeModifier> combined = AttributeUtils.combineAttributes(event.getOriginalModifiers(), qualityModifiers);
        event.clearModifiers();
        combined.forEach(event::addModifier);
    }

    @SubscribeEvent
    public static void addToolBaseStats(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!LivingEntity.getEquipmentSlotForItem(stack).equals(event.getSlotType())) return;

        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();

        List<AttributeAddition> additions = null;
        if (stack.getTags().anyMatch(t -> t.equals(ModTags.Items.GOLDEN_SWORDS))) {
            if (Config.goldenSwordMobDropQuality > 0)
                additions = List.of(new AttributeAddition("qualitycrafting:mob_drop_quality", Config.goldenSwordMobDropQuality, "ADDITION"));
        } else if (stack.getTags().anyMatch(t -> t.equals(ModTags.Items.GOLDEN_PICKAXES))) {
            additions = buildGoldenPickaxeAdditions();
        } else if (stack.getTags().anyMatch(t -> t.equals(ModTags.Items.GOLDEN_HOES))) {
            additions = buildGoldenHoeAdditions();
        }
        if (additions == null || additions.isEmpty()) return;

        Multimap<Attribute, AttributeModifier> baseModifiers = buildModifiers(additions, itemId, 0);
        Multimap<Attribute, AttributeModifier> combined = AttributeUtils.combineAttributes(event.getOriginalModifiers(), baseModifiers);
        event.clearModifiers();
        combined.forEach(event::addModifier);
    }

    // Fixed UUIDs so the same modifier isn't double-applied across ticks
    private static final UUID ENCHANT_QUALITY_UUID      = UUID.fromString("a1b2c3d4-0001-0000-0000-000000000001");
    private static final UUID ENCHANT_PROFICIENCY_UUID  = UUID.fromString("a1b2c3d4-0001-0000-0000-000000000002");
    private static final UUID ENCHANT_YIELD_UUID        = UUID.fromString("a1b2c3d4-0001-0000-0000-000000000003");

    @SubscribeEvent
    public static void addEnchantmentAttributes(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEnchanted()) return;
        if (!event.getSlotType().equals(net.minecraft.world.entity.EquipmentSlot.MAINHAND)) return;

        int qualityLevel      = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.CRAFTING_QUALITY.get(), stack);
        int proficiencyLevel  = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.CRAFTING_PROFICIENCY.get(), stack);
        int yieldLevel        = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.CRAFTING_YIELD.get(), stack);

        if (qualityLevel == 0 && proficiencyLevel == 0 && yieldLevel == 0) return;

        Multimap<Attribute, AttributeModifier> additions = HashMultimap.create();
        if (qualityLevel > 0) {
            additions.put(net.leahperson.proficientmod.attribute.ModAttributes.QUALITY.get(),
                    new AttributeModifier(ENCHANT_QUALITY_UUID, "Crafting Quality Enchantment",
                            qualityLevel * 5.0, AttributeModifier.Operation.ADDITION));
        }
        if (proficiencyLevel > 0) {
            additions.put(net.leahperson.proficientmod.attribute.ModAttributes.PROFICIENCY.get(),
                    new AttributeModifier(ENCHANT_PROFICIENCY_UUID, "Crafting Proficiency Enchantment",
                            proficiencyLevel * 5.0, AttributeModifier.Operation.ADDITION));
        }
        if (yieldLevel > 0) {
            additions.put(net.leahperson.proficientmod.attribute.ModAttributes.YIELD.get(),
                    new AttributeModifier(ENCHANT_YIELD_UUID, "Crafting Yield Enchantment",
                            yieldLevel * 5.0, AttributeModifier.Operation.ADDITION));
        }

        Multimap<Attribute, AttributeModifier> combined = AttributeUtils.combineAttributes(event.getOriginalModifiers(), additions);
        event.clearModifiers();
        combined.forEach(event::addModifier);
    }

    private static List<AttributeAddition> buildGoldenPickaxeAdditions() {
        List<AttributeAddition> list = new java.util.ArrayList<>();
        if (Config.goldenPickaxeMiningQuality > 0)
            list.add(new AttributeAddition("qualitycrafting:mining_quality", Config.goldenPickaxeMiningQuality, "ADDITION"));
        if (Config.goldenPickaxeMiningYield > 0)
            list.add(new AttributeAddition("qualitycrafting:mining_yield", Config.goldenPickaxeMiningYield, "ADDITION"));
        return list;
    }

    private static List<AttributeAddition> buildGoldenHoeAdditions() {
        List<AttributeAddition> list = new java.util.ArrayList<>();
        if (Config.goldenHoeFarmingQuality > 0)
            list.add(new AttributeAddition("qualitycrafting:farming_quality", Config.goldenHoeFarmingQuality, "ADDITION"));
        if (Config.goldenHoeFarmingYield > 0)
            list.add(new AttributeAddition("qualitycrafting:farming_yield", Config.goldenHoeFarmingYield, "ADDITION"));
        return list;
    }

    private static Multimap<Attribute, AttributeModifier> buildModifiers(
            List<AttributeAddition> additions, ResourceLocation itemId, int qualityLevel) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        for (AttributeAddition addition : additions) {
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.parse(addition.attribute_id()));
            if (attribute == null) continue;
            AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(addition.operation());
            modifiers.put(attribute, new AttributeModifier(
                    Mth.createInsecureUUID(RandomSource.create(
                            (itemId + ProficientMod.MOD_ID + qualityLevel + addition.attribute_id()).hashCode())),
                    ProficientMod.MOD_ID + " " + attribute.getDescriptionId(),
                    addition.amount(),
                    operation));
        }
        return modifiers;
    }
}
