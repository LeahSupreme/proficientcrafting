package net.leahperson.proficientmod.datagen;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.ModBlocks;
import net.leahperson.proficientmod.effects.ModEffects;
import net.leahperson.proficientmod.enchantment.ModEnchantments;
import net.leahperson.proficientmod.item.ModItems;
import net.leahperson.proficientmod.potion.ModPotions;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModLanguageProvider extends LanguageProvider {

    private final String modId = ProficientMod.MOD_ID;

    public ModLanguageProvider(PackOutput output) {
        super(output, ProficientMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addSimpleItemName(ModItems.AMETHYST_PICKAXE);

        addSimpleItemName(ModItems.STONE_FORGE_HAMMER);
        addSimpleItemName(ModItems.IRON_FORGE_HAMMER);
        addSimpleItemName(ModItems.DIAMOND_FORGE_HAMMER);
        addSimpleItemName(ModItems.GOLD_FORGE_HAMMER);
        addSimpleItemName(ModItems.AMETHYST_FORGE_HAMMER);
        addSimpleItemName(ModItems.NETHERITE_FORGE_HAMMER);

        addSimpleItemName(ModItems.WOODEN_LADLE);
        addSimpleItemName(ModItems.IRON_LADLE);
        addSimpleItemName(ModItems.DIAMOND_LADLE);
        addSimpleItemName(ModItems.GOLD_LADLE);
        addSimpleItemName(ModItems.AMETHYST_LADLE);
        addSimpleItemName(ModItems.NETHERITE_LADLE);

        addSimpleItemName(ModItems.IRON_CHISEL);
        addSimpleItemName(ModItems.DIAMOND_CHISEL);
        addSimpleItemName(ModItems.GOLD_CHISEL);
        addSimpleItemName(ModItems.NETHERITE_CHISEL);

        addSimpleItemName(ModItems.IRON_SAW);
        addSimpleItemName(ModItems.GOLD_SAW);
        addSimpleItemName(ModItems.DIAMOND_SAW);
        addSimpleItemName(ModItems.AMETHYST_SAW);
        addSimpleItemName(ModItems.NETHERITE_SAW);

        addSimpleItemName(ModItems.IRON_TIPPED_QUILL);
        addSimpleItemName(ModItems.DIAMOND_TIPPED_QUILL);
        addSimpleItemName(ModItems.NETHERITE_TIPPED_QUILL);

        addSimpleItemName(ModItems.AMETHYST_SCEPTER);
        addSimpleItemName(ModItems.DIAMOND_SCEPTER);
        addSimpleItemName(ModItems.EMERALD_SCEPTER);
        addSimpleItemName(ModItems.NETHER_STAR_SCEPTER);

        addSimpleItemName(ModItems.LAPIS_RING);
        addSimpleItemName(ModItems.DIAMOND_RING);
        addSimpleItemName(ModItems.EMERALD_RING);
        addSimpleItemName(ModItems.AMETHYST_RING);
        addSimpleItemName(ModItems.NETHER_STAR_RING);

        addSimpleItemName(ModItems.SMITHING_CHARM);

        addSimpleItemName(ModItems.CRAFTERS_STEW);
        addSimpleItemName(ModItems.ENERGETIC_SODA);
        addSimpleItemName(ModItems.WEDDING_CAKE);

        addSimpleNameBlock(ModBlocks.FORGING_STATION);
        addSimpleNameBlock(ModBlocks.COOKING_POT);
        addSimpleNameBlock(ModBlocks.SCRIBING_TABLE);
        addSimpleNameBlock(ModBlocks.JEWELCRAFTING_STATION);
        addSimpleNameBlock(ModBlocks.WORKBENCH);
        addSimpleNameBlock(ModBlocks.REFORGING_ALTAR);

        addSimpleNameEffect(ModEffects.QUALITY);
        addSimpleNameEffect(ModEffects.PROFICIENCY);
        addSimpleNameEffect(ModEffects.YIELD);
        addSimpleNameEffect(ModEffects.MOB_DROP_QUALITY);
        addSimpleNameEffect(ModEffects.MOB_DROP_YIELD);
        addSimpleNameEffect(ModEffects.FARMING_QUALITY);
        addSimpleNameEffect(ModEffects.FARMING_YIELD);
        addSimpleNameEffect(ModEffects.MINING_QUALITY);
        addSimpleNameEffect(ModEffects.MINING_YIELD);

        addSimpleNameEnchant(ModEnchantments.CRAFTING_QUALITY);
        addSimpleNameEnchant(ModEnchantments.CRAFTING_PROFICIENCY);
        addSimpleNameEnchant(ModEnchantments.CRAFTING_YIELD);

        addPotionTranslations(ModPotions.QUALITY, "Quality");
        addPotionTranslations(ModPotions.LONG_QUALITY, "Quality");
        addPotionTranslations(ModPotions.STRONG_QUALITY, "Quality");
        addPotionTranslations(ModPotions.PROFICIENCY, "Proficiency");
        addPotionTranslations(ModPotions.LONG_PROFICIENCY, "Proficiency");
        addPotionTranslations(ModPotions.STRONG_PROFICIENCY, "Proficiency");
        addPotionTranslations(ModPotions.YIELD, "Yield");
        addPotionTranslations(ModPotions.LONG_YIELD, "Yield");
        addPotionTranslations(ModPotions.STRONG_YIELD, "Yield");
        addPotionTranslations(ModPotions.MOB_DROP_QUALITY, "Mob Drop Quality");
        addPotionTranslations(ModPotions.LONG_MOB_DROP_QUALITY, "Mob Drop Quality");
        addPotionTranslations(ModPotions.STRONG_MOB_DROP_QUALITY, "Mob Drop Quality");
        addPotionTranslations(ModPotions.MOB_DROP_YIELD, "Mob Drop Yield");
        addPotionTranslations(ModPotions.LONG_MOB_DROP_YIELD, "Mob Drop Yield");
        addPotionTranslations(ModPotions.STRONG_MOB_DROP_YIELD, "Mob Drop Yield");
        addPotionTranslations(ModPotions.FARMING_QUALITY, "Farming Quality");
        addPotionTranslations(ModPotions.LONG_FARMING_QUALITY, "Farming Quality");
        addPotionTranslations(ModPotions.STRONG_FARMING_QUALITY, "Farming Quality");
        addPotionTranslations(ModPotions.FARMING_YIELD, "Farming Yield");
        addPotionTranslations(ModPotions.LONG_FARMING_YIELD, "Farming Yield");
        addPotionTranslations(ModPotions.STRONG_FARMING_YIELD, "Farming Yield");
        addPotionTranslations(ModPotions.MINING_QUALITY, "Mining Quality");
        addPotionTranslations(ModPotions.LONG_MINING_QUALITY, "Mining Quality");
        addPotionTranslations(ModPotions.STRONG_MINING_QUALITY, "Mining Quality");
        addPotionTranslations(ModPotions.MINING_YIELD, "Mining Yield");
        addPotionTranslations(ModPotions.LONG_MINING_YIELD, "Mining Yield");
        addPotionTranslations(ModPotions.STRONG_MINING_YIELD, "Mining Yield");

        add("creativetab.proficient_tab", "Quality Crafting");

        add("block.qualitycrafting.station.full", "The table is full");

        add("qualitycrafting.rarity.0", "Common");
        add("qualitycrafting.rarity.1", "Uncommon");
        add("qualitycrafting.rarity.2", "Rare");
        add("qualitycrafting.rarity.3", "Legendary");

        add("qualitycrafting.jei.levelcost", "Level Cost: ");
        add("qualitycrafting.jei.proficiencycost", "Proficiency required: ");
        add("qualitycrafting.jei.qualitycost", "Quality required: ");
        add("qualitycrafting.jei.yieldcost", "+%s Output per %s Yield");
        add("qualitycrafting.jei.rarityinputtooltip", "+%s Quality if %s");
        add("qualitycrafting.jei.cooktime", "Cook Time: ");
        add("qualitycrafting.jei.crafttime", "Craft Time: ");
        add("qualitycrafting.jei.reforging.hint", "Place quality item, then activate with scepter");

        add("qualitycrafting:proficiency", "Proficiency");
        add("qualitycrafting:proficiency.desc", "Determines which items you can forge.");
        add("qualitycrafting:quality", "Quality");
        add("qualitycrafting:quality.desc", "Improves the result of forged items.");
        add("qualitycrafting:yield", "Yield");
        add("qualitycrafting:yield.desc", "Improves the amount of forged items created.");

        add("qualitycrafting:farming_quality", "Farming Quality");
        add("qualitycrafting:farming_quality.desc", "Improves the rarity of harvested crops.");
        add("qualitycrafting:farming_yield", "Farming Yield");
        add("qualitycrafting:farming_yield.desc", "Increases crop drops, stacking with Fortune.");
        add("qualitycrafting:mining_quality", "Mining Quality");
        add("qualitycrafting:mining_quality.desc", "Improves the rarity of mined drops.");
        add("qualitycrafting:mining_yield", "Mining Yield");
        add("qualitycrafting:mining_yield.desc", "Increases ore drops, stacking with Fortune.");
        add("qualitycrafting:mob_drop_quality", "Mob Drop Quality");
        add("qualitycrafting:mob_drop_quality.desc", "Improves the rarity of mob drops.");
        add("qualitycrafting:mob_drop_yield", "Mob Drop Yield");
        add("qualitycrafting:mob_drop_yield.desc", "Increases mob drops, stacking with Looting.");

        add("qualitycrafting.station.notproficient", "Your Proficiency is too low. Needed: %s Current: %s");
        add("qualitycrafting.station.noexperience", "You need more experience.");
        add("qualitycrafting.reforging.noitem", "Place a quality item in the altar first.");

        add("qualitycrafting.tooltip.food.when_consumed", "When consumed:");
        add("qualitycrafting.tooltip.food.effect", "%s (%s)");
    }

    public void addSimpleItemName(Supplier<? extends Item> supplier) {
        Item item = supplier.get();
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        if (itemId != null) {
            add(item, toName(itemId.getPath()));
        } else {
            throw new IllegalStateException("Item not registered: " + item);
        }
    }

    public void addSimpleNameBlock(Supplier<? extends Block> supplier) {
        Block block = supplier.get();
        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
        if (blockId != null) {
            add(block, toName(blockId.getPath()));
        } else {
            throw new IllegalStateException("Block not registered: " + block);
        }
    }

    public void addSimpleNameEffect(Supplier<? extends MobEffect> supplier) {
        MobEffect effect = supplier.get();
        ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        if (effectId != null) {
            add(effect, toName(effectId.getPath()));
        } else {
            throw new IllegalStateException("ModEffect not registered: " + effect);
        }
    }

    public void addSimpleNameEnchant(Supplier<? extends Enchantment> supplier) {
        Enchantment enchantment = supplier.get();
        ResourceLocation enchantmentId = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
        if (enchantmentId != null) {
            add(enchantment, toName(enchantmentId.getPath()));
        } else {
            throw new IllegalStateException("Enchantment not registered: " + enchantment);
        }
    }

    public void addPotionTranslations(Supplier<? extends Potion> potionSupplier, String baseName) {
        Potion potion = potionSupplier.get();
        if (ForgeRegistries.POTIONS.getKey(potion) == null) {
            throw new IllegalStateException("Potion not registered: " + potion);
        }
        String potionTranslationKey = potion.getName("");
        add("item.minecraft.potion.effect." + potionTranslationKey, "Potion of " + baseName);
        add("item.minecraft.splash_potion.effect." + potionTranslationKey, "Splash Potion of " + baseName);
        add("item.minecraft.lingering_potion.effect." + potionTranslationKey, "Lingering Potion of " + baseName);
    }

    public void addItemKey(String path, String value) {
        add("item." + modId + "." + path, value);
    }

    public void addAttributeKey(String path, String value) {
        add("attribute.name." + modId + "." + path, value);
    }

    public static String toName(String registryPath) {
        StringBuilder stringBuilder = new StringBuilder(registryPath.length() + 8);
        for (String part : registryPath.split("_")) {
            if (part.isEmpty()) continue;
            stringBuilder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) stringBuilder.append(part.substring(1));
            stringBuilder.append(' ');
        }
        return stringBuilder.toString().trim();
    }
}
